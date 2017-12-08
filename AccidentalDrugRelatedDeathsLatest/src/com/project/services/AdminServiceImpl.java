package com.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;
import com.project.dao.AdminDao;
import com.project.models.DeathPersonDetails;
import com.project.models.DeathPersonDetailsPrivate;
import com.project.models.RegUser;
import com.project.models.UploadedFile;
import com.project.models.User;

@Service
public class AdminServiceImpl implements AdminService{
	
	private AdminDao adminDao;
	
	@Autowired
	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}

	public User adminLogin(User user) {
		return adminDao.amdinLogin(user);
	}

	public List<RegUser> getAllRegUsers() {
		return adminDao.getAllRegUsers();
	}

	@Override
	public List<UploadedFile> getUnverifiedFiles() {
		return adminDao.getUnverifiedFiles();
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword, String username) {
			int i = adminDao.changePassword(oldPassword, newPassword, username);
			return (i == 1)?true:false;
	}

	@Override
	public boolean blockOrUnblockUser(String email, String userId, String status) {
			return (adminDao.blockOrUnblockUser(email, userId, status) == 1)?true:false;
	}

	@Override
	public String getFilePath(String fileId) {
		return adminDao.getFilePath(fileId);
	}

	@Override
	public boolean acceptFile(String fileId) {
		return (adminDao.acceptFile(fileId) == 1)?true:false;
	}
	
	@Override
	public boolean insertFileData(List<DeathPersonDetails> list) {
		try{
			adminDao.insertFileData(list);
			return true;
		}catch(MongoException e){
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	

	@Override
	public boolean insertPrivateFileData(List<DeathPersonDetailsPrivate> list) {
		try{
			adminDao.insertPrivateFileData(list);
			return true;
		}catch(MongoException e){
			System.out.println(e.getMessage());
		}
		return false;
		
	}

	@Override
	public boolean rejectFile(String fileId, String reason) {
		return (adminDao.rejectFile(fileId, reason) == 1)?true:false;
	}

	@Override
	public List<UploadedFile> getUserFiles(String email) {
		return adminDao.getUserFiles(email);
	}

	@Override
	public String getFileVisibility(String fileId) {
		return adminDao.getFileVisibility(fileId);
	}
	
	
	
}
