package codelab.web.controller

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Locale
import scala.collection.JavaConversions._
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.ProgressListener
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartHttpServletRequest
import codelab.service.AppService
import core.service.FormatService
import core.service.IOService
import javax.annotation.Resource
import javax.servlet.http.HttpServletResponse
import resource.managed
import org.springframework.web.bind.annotation.RequestMethod
import java.io.InputStreamReader
import org.springframework.web.servlet.ModelAndView
import scala.collection.mutable._
import core.BaseComponent

@Controller
class FileController extends BaseComponent {

	@Resource var appService : AppService = _
	@Resource var formatService : FormatService = _
	@Resource var ioService : IOService = _

	@ModelAttribute("app") def app = appService
	@ModelAttribute("fmt") def fmt = formatService

	@RequestMapping(value = Array("/files", "/download"), method = Array(RequestMethod.GET))
	def downloadPage(@RequestParam(required = false, defaultValue = ".") path: String, response: HttpServletResponse, model: Model) : ModelAndView = {
		val file = new File(path)
		model.addAttribute("path", file)
		if (file.isFile) {
			if (!formatService.isTextFile(file)) {
				response.setHeader("Content-disposition", "attachment;filename=" + file.getName)
				response.setContentLength(file.length.toInt)
			}
			for (is <- managed(new FileInputStream(file)); os <- managed(response.getOutputStream)) {
				ioService.copy(is, os)
			}
		} else if (file.isDirectory) {
			val childFiles = file.listFiles.filter(file => !file.isHidden).sortWith((f1,f2) =>
				if (f1.isDirectory && f2.isFile) true
				else if (f1.isFile && f2.isDirectory) false
				else f1.getName < f2.getName)
			model.addAttribute("files", childFiles)
			return new ModelAndView("files")
		}
		null
	}

	@RequestMapping(value = Array("/test", "/upload", "/multiupload"), method = Array(RequestMethod.GET))
	def uploadPage(model: Model): String = "upload"

	@RequestMapping(value = Array("/upload"), method = Array(RequestMethod.POST), produces = Array("application/json;charset=utf-8"))
	@ResponseBody
	def handleMultiFileUpload(request: MultipartHttpServletRequest, locale: Locale, model: Model) : java.util.Map[String,Any] = {
		val result = new HashMap[String,Any]
		val isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {
			val remoteAddr = request.getRemoteAddr
			val curDateTime = formatService.formatDate(Calendar.getInstance.getTime, appService.defaultArchiveDateFormat)

			// Create a factory for disk-based file items
			//val factory = new DiskFileItemFactory
			// Set factory constraints
			//val sizeThreshold = appService.prop("upload.sizeThreshold").toInt
			//val sizeMax = appService.prop("upload.sizeMax").toInt
			//val repository = appService.prop("upload.repository")

			//factory.setSizeThreshold(sizeThreshold);
			//if (repository != null)
			///	factory.setRepository(new File(repository));

			// Create a new file upload handler
			//val upload = new ServletFileUpload(factory);
			// Set overall request size constraint
			//upload.setSizeMax(sizeMax);
			// Create a progress listener
			val progressListener = new ProgressListener {
				override def update(pBytesRead: Long, pContentLength: Long, pItems: Int) {
					logger.info(s"We are currently reading item ${pItems}");
					if (pContentLength == -1) {
						logger.info(s"So far, ${pBytesRead} bytes have been read." )
					} else {
						logger.info(s"So far, ${pBytesRead} of ${pContentLength} bytes have been read.");
					}
				}
			}
			//upload.setProgressListener(progressListener);
			//val items = upload.parseRequest(request);

			val fileNames = request.getFileNames
			logger.info(s"# files = ${fileNames.size}")

			val uploadInfos = new ListBuffer[java.util.Map[String,Any]]
			for (fileName <- request.getFileNames) {
				for (file <- request.getFiles(fileName)) {
					if (!file.getOriginalFilename.isEmpty) {
						var uploadInfo = new HashMap[String,Any]
						val outputFile = new File(appService.uploadDir + "/" + curDateTime + "_" + remoteAddr, file.getOriginalFilename)
						ioService.createParentDirs(outputFile)
						try {
							val bytes = file.getBytes
							for (stream <- managed(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
								stream.write(bytes)
							}
							logger.info(s"Uploaded ${file.getOriginalFilename} (${file.getSize} bytes) from ${remoteAddr} to ${outputFile}.")
							uploadInfo.put("name", file.getName)
							uploadInfo.put("fileName", file.getOriginalFilename)
							uploadInfo.put("contentType", file.getContentType)
							uploadInfo.put("size", file.getSize)
							uploadInfo.put("code", "SUCCESS")
						} catch {
							case e: Exception =>
								uploadInfo.put("code", "FAILED")
								uploadInfo.put("errmsg", s"You failed to upload ${file.getName} => ${e.getMessage}")
						}
						uploadInfos.add(uploadInfo)
					}
				}
			}

			result.put("message", appService.msg("upload.completed", locale))
			result.put("data", uploadInfos.toArray)

			result.get("message") match {
				case Some(m) => logger.info(m.toString);
				case None =>
			}

			/**
			// Parse the request
			try {
				val items = upload.parseRequest(request);
				val uploadInfos = new Array[java.util.Map[String,Any]](items.size)
				// Process the uploaded items
				items.zipWithIndex.foreach { case (item,i) =>
			//		if (item.isFormField) {
				//		val outputFile = new File(appService.uploadDir + "/" + curDateTime, item.getName)
				//		uploadInfos(i) = processUploadedFile(outputFile, item)
				//	}
				}
				result.put("code", "SUCCESS")
				result.put("message", "Upload completed.")
				result.put("data", uploadInfos);
				logger.info("{}", "Upload completed.")
			} catch {
				case e: Exception => logger.error(e.toString(), e);
			}*/
		} else {
			result.put("code", "FAILED");
			result.put("message", appService.msg("upload.multipartContentExpected", locale));
			logger.info("Multipart content expected.");
		}
		result
	}

	def processUploadedFile(outputFile: File, item: FileItem) : java.util.Map[String,Any] = {
    	ioService.createParentDirs(outputFile)
		item.write(outputFile);
		logger.info("File " + outputFile.getPath() + " created.");
        val uploadInfo = new HashMap[String,Any]
        uploadInfo.put("fieldName", item.getFieldName)
        uploadInfo.put("fileName", item.getName)
        uploadInfo.put("contentType", item.getContentType)
        uploadInfo.put("isInMemory", item.isInMemory)
        uploadInfo.put("size", item.getSize)
		uploadInfo
	}
}