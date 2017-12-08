package com.project.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.models.DeathPersonDetails;
import com.project.models.UploadedFile;
import com.project.models.User;
import com.project.services.AdminService;

@Controller
public class AdminController {
	private AdminService admService;

	@Resource
	private ServletContext servletContext;

	@Autowired
	public void setAdmService(AdminService admService) {
		this.admService = admService;
	}

	@RequestMapping(value = "/")
	public String defulat() {
		return "index";
	}

	@RequestMapping(value = "/admin/login-process", method = RequestMethod.POST)
	public String loginProcess(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest req) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		User adminUser = admService.adminLogin(user);
		if (adminUser != null) {
			HttpSession session = req.getSession();
			session.setAttribute("admin", adminUser);
			return "redirect:/admin/dashboard";
		} else {
			req.getSession().setAttribute("loginError", true);
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/admin/change-password")
	public String changePassword(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		return "admin-change-password";
	}

	@RequestMapping(value = "/admin/update-password", method = RequestMethod.POST)
	public String updatePassword(HttpServletRequest req, Model m, @RequestParam("oldPswd") String oldPassword,
			@RequestParam("newPswd") String newPassword) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		boolean b = admService.changePassword(oldPassword, newPassword, user.getUsername());
		session.setAttribute("pswdUpdateFlag", b);
		return "redirect:/admin/change-password";
	}

	@RequestMapping(value = "/admin/dashboard")
	public String goToDashboard(HttpServletRequest req, Model m) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		m.addAttribute("unverifiedFiles", admService.getUnverifiedFiles());
		return "admin-dashboard";
	}

	@RequestMapping(value = "/admin/registered-users")
	public String getAllRegUsers(HttpServletRequest req, Model m) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		m.addAttribute("regUsers", admService.getAllRegUsers());
		return "reg-users";
	}

	@RequestMapping(value = "/admin/logout")
	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		session.removeAttribute("admin");
		session.invalidate();
		resp.addHeader("Cache-control", "no-store, must-revalidate, private,no-cache");
		resp.addHeader("Pragma", "no-cache");
		resp.addHeader("Expires", "0");
		return "redirect:/";
	}

	@RequestMapping(value = "/block-or-unblock/{statusUpdateTo}/{userId}/{email}")
	public String blockOrUnblockUser(HttpServletRequest req, @PathVariable("statusUpdateTo") String status,
			@PathVariable("userId") String userId, @PathVariable("email") String email) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		boolean b = admService.blockOrUnblockUser(email, userId, status);
		if (b) {
			System.out.println("user " + email + " status updated");

		} else {
			System.out.println("user " + email + " status not updated");
		}
		session.setAttribute("bOrub", b);
		session.setAttribute("email", email);
		session.setAttribute("status", status);
		return "redirect:/admin/registered-users";
	}

	
	@RequestMapping(value = "/admin/{fileId}/accept")
	public String acceptFile(HttpServletRequest req, @PathVariable("fileId") String fileId) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		String visibility = admService.getFileVisibility(fileId);

		String filePath = admService.getFilePath(fileId);
		String fullFilePath = servletContext.getRealPath(filePath);
		if ("public".equals(visibility)) {
			List<DeathPersonDetails> list = new ArrayList<DeathPersonDetails>();
			try {
				BufferedReader br = new BufferedReader(new FileReader(fullFilePath));
				String line = null;
				try {
					br.readLine();
					while ((line = br.readLine()) != null) {
						DeathPersonDetails dp = new DeathPersonDetails();
						String[] l = line.split(",");
						if (l.length > 26) {
							String subLine1 = line.substring(0, line.lastIndexOf(",\""));
							String subLine2 = line.substring(line.lastIndexOf(",\""), line.length()).replace(",\"", ",");
							line = subLine1+subLine2;
							l = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
							dp.setCaseNumber(l[0]);
							dp.setSex(l[2]);
							dp.setRace(l[3]);
							try {
								dp.setAge(Integer.parseInt(l[4]));
							} catch (NumberFormatException nfe) {}
							dp.setDeathCity(l[8]);
							dp.setDeathState(l[9]);
							dp.setCause(l[14]);
							dp.setHeroin(l[15]);
							dp.setCocaine(l[16]);
							dp.setFentanyl(l[17]);
							dp.setOxycodone(l[18]);
							dp.setOxymorphone(l[19]);
							dp.setEtOH(l[20]);
							dp.setHydro_codeine(l[21]);
							dp.setBenzodiazepine(l[22]);
							dp.setMethadone(l[23]);
							dp.setAmphet(l[24]);
							dp.setTramad(l[25]);
							dp.setMorphine(l[26]);
							list.add(dp);
						}
					}
					br.close();

					boolean b = admService.insertFileData(list);
					if (b) {
						System.out.println("file accepted");
						admService.acceptFile(fileId);
					} else {
						System.out.println("file not accepted, due to some reasons");
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else{
			admService.acceptFile(fileId);
		}

		return "redirect:/admin/dashboard";
	}
	
	
	//Backup method
	/*@RequestMapping(value = "/admin/{fileId}/accept")
	public String acceptFile(HttpServletRequest req, @PathVariable("fileId") String fileId) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		String visibility = admService.getFileVisibility(fileId);

		String filePath = admService.getFilePath(fileId);
		String fullFilePath = servletContext.getRealPath(filePath);
		if ("public".equals(visibility)) {
			List<DeathPersonDetails> list = new ArrayList<DeathPersonDetails>();
			try {
				BufferedReader br = new BufferedReader(new FileReader(fullFilePath));
				String line = null;
				try {
					br.readLine();
					while ((line = br.readLine()) != null) {
						DeathPersonDetails dp = new DeathPersonDetails();
						String[] l = line.split(",");
						// System.out.println(Arrays.toString(l));
						if (l.length >= 14) {
							dp.setCaseNumber(l[0]);
							dp.setSex(l[2]);
							dp.setRace((l[3].contains("Hispanic")) ? "Hispanic, White" : l[3]);
							try {
								dp.setAge(Integer.parseInt((l[3].contains("Hispanic")) ? l[5] : l[4]));
							} catch (NumberFormatException nfe) {
							}
							if (!l[3].contains("Hispanic")) {
								dp.setDeathState(l[9]);
								dp.setCause(l[14]);
								dp.setHeroin(l[15]);
								dp.setCocaine(l[16]);
								dp.setFentanyl(l[17]);
								dp.setOxycodone(l[18]);
								dp.setOxymorphone(l[19]);
								dp.setEtOH(l[20]);
								dp.setHydro_codeine(l[21]);
								dp.setBenzodiazepine(l[22]);
								dp.setMethadone(l[23]);
								dp.setAmphet(l[24]);
								dp.setTramad(l[25]);
								dp.setMorphine(l[26]);
							} else {
								dp.setDeathState(l[10]);
								dp.setCause(l[15]);
								dp.setHeroin(l[16]);
								dp.setCocaine(l[17]);
								dp.setFentanyl(l[18]);
								dp.setOxycodone(l[19]);
								dp.setOxymorphone(l[20]);
								dp.setEtOH(l[21]);
								dp.setHydro_codeine(l[22]);
								dp.setBenzodiazepine(l[23]);
								dp.setMethadone(l[24]);
								dp.setAmphet(l[25]);
								dp.setTramad(l[26]);
								dp.setMorphine(l[27]);
							}
							list.add(dp);
						}
					}
					System.out.println(list);
					br.close();

					boolean b = admService.insertFileData(list);
					if (b) {
						System.out.println("file accepted");
						admService.acceptFile(fileId);
					} else {
						System.out.println("file not accepted, due to some reasons");
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			admService.acceptFile(fileId);
		}

		return "redirect:/admin/dashboard";
	}*/

	@RequestMapping(value = "/admin/{fileId}/reject/{reason}")
	public String rejectFile(HttpServletRequest req, @PathVariable("fileId") String fileId,
			@PathVariable("reason") String reason) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		boolean b = admService.rejectFile(fileId, reason);
		if (b) {
			System.out.println("file rejected");
		} else {
			System.out.println("file not rejected, due to some reasons");
		}
		return "redirect:/admin/dashboard";
	}

	@RequestMapping(value = "/admin/{fileId}/download")
	public void downloadFile(HttpServletResponse resp, @PathVariable("fileId") String fileId) throws IOException {
		String filePath = servletContext.getRealPath(admService.getFilePath(fileId));
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

	@RequestMapping(value = "/admin/user-files/{email:.+}")
	public String getUserFiles(HttpServletRequest req, Model m, @PathVariable("email") String email) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("admin");
		if (user == null)
			return "redirect:/";
		List<UploadedFile> files = admService.getUserFiles(email);
		m.addAttribute("userFiles", files);
		return "admin-user-files";
	}

}
