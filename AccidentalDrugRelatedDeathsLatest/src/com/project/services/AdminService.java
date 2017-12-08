package com.project.services;

import java.util.List;

import com.project.models.DeathPersonDetails;
import com.project.models.DeathPersonDetailsPrivate;
import com.project.models.RegUser;
import com.project.models.UploadedFile;
import com.project.models.User;

public interface AdminService {
	User adminLogin(User user);
	List<RegUser> getAllRegUsers();
	List<UploadedFile> getUnverifiedFiles();
	boolean changePassword(String oldPassword, String newPassword, String username);
	boolean blockOrUnblockUser(String email, String userId, String status);
	String getFilePath(String fileId);
	boolean acceptFile(String fileId);
	boolean insertFileData(List<DeathPersonDetails> list);
	boolean insertPrivateFileData(List<DeathPersonDetailsPrivate> list);
	boolean rejectFile(String fileId, String reason);
	List<UploadedFile> getUserFiles(String email);
	String getFileVisibility(String fileId);
}
