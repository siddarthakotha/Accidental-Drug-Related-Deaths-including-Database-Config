package com.project.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dao.UserDao;
import com.project.models.DeathPersonDetails;
import com.project.models.RegUser;
import com.project.models.UploadedFile;
import com.project.models.UserUpdatePassword;

@Service
public class UserServiceImple implements UserService {

	private UserDao userDao;

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public RegUser getUser(String email) {
		return userDao.isUserExists(email);
	}

	@Override
	public boolean registerUser(RegUser user) throws Exception {
		RegUser u = userDao.isUserExists(user.getEmail());
		if (u == null) {
			try {
				userDao.registerUser(user);
				return true;
			} catch (Exception e) {
				if ("com.mongodb.MongoException$DuplicateKey".equals(e.getMessage())) {
					return true;
				}
			}
		} else {
			throw new Exception("User already registered");
		}
		return false;
	}

	@Override
	public RegUser loginUser(RegUser user) {
		return userDao.loginUser(user);
	}

	@Override
	public boolean changePassword(UserUpdatePassword data) {
		return (userDao.changePassword(data) == 1) ? true : false;
	}

	@Override
	public boolean addFile(UploadedFile file) {
		try {
			userDao.addFile(file);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if ("com.mongodb.MongoException$DuplicateKey".equals(e.getMessage())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<UploadedFile> getUserUploadedFiles(String email) {
		List<UploadedFile> dbFilesList = userDao.getUserUploadedFiles(email);
		List<UploadedFile> listOfFiles = new ArrayList<UploadedFile>();
		for(UploadedFile file : dbFilesList){
			file.setRowId(file.getId().toString());
			listOfFiles.add(file);
		}
		return listOfFiles;
	}

	@Override
	public List<DeathPersonDetails> getData(int fromAge, int toAge) {
		return userDao.getData(fromAge, toAge);
	}

	@Override
	public List<DeathPersonDetails> getData(String sex, String city) {
		return userDao.getData(sex, city);
	}

	@Override
	public List<DeathPersonDetails> getData() {
		List<DeathPersonDetails> list = null;
		final String w = "White";
		final String b = "Black";
		final String hw = "Hispanic, White";
		int wc = 0, bc = 0, hwc = 0;
		wc = userDao.getNoOfDocsByRace(w);
		bc = userDao.getNoOfDocsByRace(b);
		hwc = userDao.getNoOfDocsByRace(hw);
		if(wc > bc && wc > hwc){
			list = userDao.getData(w);
		}else if(bc > wc && bc > hwc){
			list = userDao.getData(b);
		}else{
			list = userDao.getData(hw);
		}
		return list;
	}

	@Override
	public List<DeathPersonDetails> getDataByDrug() {
		String[] drugs = {"heroin","cocaine","fentanyl","oxycodone","oxymorphone","etOH","hydro_codeine","benzodiazepine","methadone","amphet","tramad","morphine"};
		int index = -1;
		int[] drugsDocCount = new int[drugs.length];
		for(int i = 0; i < drugs.length; i++){
			drugsDocCount[i] = userDao.getNoOfDocsByDrug(drugs[i]);
		}
		index = this.returnHighestValueIndexs(drugsDocCount);
		List<DeathPersonDetails> l = userDao.getDataByDrug(drugs[index]);
		Iterator<DeathPersonDetails> itr = l.iterator();
		List<DeathPersonDetails> list = new ArrayList<DeathPersonDetails>();
		while(itr.hasNext()){
			DeathPersonDetails ob = itr.next();
			ob.setHighestDrugFound(drugs[index]);
			list.add(ob);
		}
		return list;
	}
	
	private int returnHighestValueIndexs(int[] originalArray){
		int arrayIndex = -1;
		int[] copiedArray =Arrays.copyOf(originalArray, originalArray.length);
		Arrays.sort(copiedArray);
		int highestValue = copiedArray[copiedArray.length-1];
		for(int index = 0; index < originalArray.length; index++){
			if(originalArray[index] == highestValue){
				arrayIndex = index;
				break;
			}
		}
		return arrayIndex;
	}
	
	

}
