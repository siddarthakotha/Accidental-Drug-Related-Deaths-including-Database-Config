package com.project.dao;

import java.util.List;

import com.project.models.DeathPersonDetails;
import com.project.models.DeathPersonDetailsPrivate;
import com.project.models.RegUser;
import com.project.models.UploadedFile;
import com.project.models.User;

public interface AdminDao {
	User amdinLogin(User user);
	List<RegUser> getAllRegUsers();
	List<UploadedFile> getUnverifiedFiles();
	int changePassword(String oldPassword, String newPassword, String username);
	int blockOrUnblockUser(String email, String userId, String status);
	String getFilePath(String fileId);
	int acceptFile(String fileId);
	void insertFileData(List<DeathPersonDetails> list);
	void insertPrivateFileData(List<DeathPersonDetailsPrivate> list);
	int rejectFile(String fileId, String reason);
	List<UploadedFile> getUserFiles(String email);
	String getFileVisibility(String fileId);
}
