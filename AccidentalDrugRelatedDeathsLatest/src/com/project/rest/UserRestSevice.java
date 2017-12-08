package com.project.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.models.DeathPersonDetails;
import com.project.models.RegUser;
import com.project.models.Response;
import com.project.models.UploadedFile;
import com.project.models.UserUpdatePassword;
import com.project.services.AdminService;
import com.project.services.UserService;

@RestController
@RequestMapping("/rest")
public class UserRestSevice {

	private static final String FILE_PATH = "/WEB-INF/uploaded-files";

	private UserService userService;
	private AdminService adminService;
	@Resource
	private ServletContext servletContext;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}



	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public RegUser registerUser(@RequestBody RegUser user) {
		RegUser loggedInUser = null;
		boolean b = false;
		try {
			 b = userService.registerUser(user);
		} catch (Exception e) {
			loggedInUser = userService.getUser(user.getEmail());
			loggedInUser.setDescription(e.getMessage());
		}
		if (b) {
			loggedInUser = userService.loginUser(user);	
		}
		return loggedInUser;
	}

	@RequestMapping(value = "/user-login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public RegUser loginUser(@RequestBody RegUser user) {
		RegUser loggedInUser = userService.loginUser(user);
		if(loggedInUser == null){
			loggedInUser = new RegUser();
			loggedInUser.setDescription("Invalid credentials");
		}
		return loggedInUser;
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response changePassword(@RequestBody UserUpdatePassword data) {
		boolean b = userService.changePassword(data);
		Response resp = new Response();
		if (b) {
			resp.setStatus("success");
			resp.setMessage("Password updated");
			resp.setErro("No Errors");
		} else {
			resp.setStatus("failed");
			resp.setMessage("Password updation failed");
			resp.setErro("Failed due to some problem.");
		}
		return resp;
	}

	@RequestMapping(value = "/upload-file", headers = ("content-type=multipart/*"), method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response uploadFile(@RequestParam("userEmail") String email, @RequestParam("visibility") String visibility,
			@RequestParam("uploadedFile") MultipartFile file) {
		String originalFileName = null;
		Response resp = new Response();
		if (!file.isEmpty()) {
			try {
				originalFileName = file.getOriginalFilename();
				File f = new File(servletContext.getRealPath(FILE_PATH));
				if (!f.exists()) {
					f.mkdirs();
				}
				File destFile = new File(f.getPath() + File.separator + originalFileName);
				file.transferTo(destFile);
				UploadedFile uFile = new UploadedFile();
				uFile.setEmail(email);
				uFile.setFileName(originalFileName);
				uFile.setFilePath(FILE_PATH + File.separator + originalFileName);
				uFile.setVisibility(visibility);
				boolean b = userService.addFile(uFile);
				if (b) {
					resp.setStatus("success");
					resp.setMessage("File " + originalFileName + " has bean uploaded");
					resp.setErro("No Errors");
				} else {
					resp.setStatus("failed");
					resp.setMessage("File " + originalFileName + " has bean uploaded, but not in database");
					resp.setErro("Error: Failed to insert in database, due to some technical problem");
				}
			} catch (Exception e) {
				resp.setStatus("failed");
				resp.setMessage("File " + originalFileName + " has not uploaded");
				resp.setErro("Error : " + e.getMessage());
			}
		} else {
			resp.setStatus("failed");
			resp.setMessage("File " + originalFileName + " has not uploaded");
			resp.setErro("Error: Trying to upload empty file");
		}
		return resp;
	}

	@RequestMapping(value = "/user-uploads", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UploadedFile> getUserUploadedFiles(@RequestParam("email") String email) {
		return userService.getUserUploadedFiles(email);
	}

	@RequestMapping(value = "/get-data-of-age-group", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeathPersonDetails> getDataByAge(@RequestParam("fromAge") int fromAge, @RequestParam("toAge") int toAge) {
		return userService.getData(fromAge, toAge);
	}

	@RequestMapping(value = "/get-data-of-people-died-from-state", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeathPersonDetails> getDataOfPeopleDiedFromStat(@RequestParam("sex") String sex,
			@RequestParam("city") String city) {
		return userService.getData(sex, city);
	}
	
	@RequestMapping(value = "/get-data-of-most-race-effected-by-drugs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeathPersonDetails> getDataOfMostRaceEffectedByDrugs(){
		return userService.getData();
	}
	
	@RequestMapping(value = "/get-data-of-most-drug-found", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DeathPersonDetails> getDataOfMostDrugFound(){
		return userService.getDataByDrug();
	}
	
	@RequestMapping(value = "/my-file/{fileId}/download")
	public void downloadFile(HttpServletResponse resp, @PathVariable("fileId") String fileId) throws IOException {
		String filePath = servletContext.getRealPath(adminService.getFilePath(fileId));
		File file = new File(filePath);
		if (!file.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			OutputStream outputStream = resp.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		resp.setContentType(mimeType);
		resp.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		resp.setContentLength((int) file.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, resp.getOutputStream());
	}

}
