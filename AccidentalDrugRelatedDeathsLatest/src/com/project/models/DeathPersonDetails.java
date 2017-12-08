package com.project.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="deathPersonDetails")
public class DeathPersonDetails {
	private String caseNumber;
	private String sex;
	private String race;
	private int age;
	private String deathCity;
	private String deathState;
	private String cause;
	private String heroin;
	private String cocaine;
	private String fentanyl;
	private String oxycodone;
	private String oxymorphone;
	private String etOH;
	private String hydro_codeine;
	private String benzodiazepine;
	private String methadone;
	private String amphet;
	private String tramad;
	private String morphine;
	private String highestDrugFound;
	
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDeathCity() {
		return deathCity;
	}
	public void setDeathCity(String deathCity) {
		this.deathCity = deathCity;
	}
	public String getDeathState() {
		return deathState;
	}
	public void setDeathState(String deathState) {
		this.deathState = deathState;
	}
	
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	public String getHeroin() {
		return heroin;
	}
	public void setHeroin(String heroin) {
		this.heroin = heroin;
	}
	public String getCocaine() {
		return cocaine;
	}
	public void setCocaine(String cocaine) {
		this.cocaine = cocaine;
	}
	public String getFentanyl() {
		return fentanyl;
	}
	public void setFentanyl(String fentanyl) {
		this.fentanyl = fentanyl;
	}
	public String getOxycodone() {
		return oxycodone;
	}
	public void setOxycodone(String oxycodone) {
		this.oxycodone = oxycodone;
	}
	public String getOxymorphone() {
		return oxymorphone;
	}
	public void setOxymorphone(String oxymorphone) {
		this.oxymorphone = oxymorphone;
	}
	public String getEtOH() {
		return etOH;
	}
	public void setEtOH(String etOH) {
		this.etOH = etOH;
	}
	public String getHydro_codeine() {
		return hydro_codeine;
	}
	public void setHydro_codeine(String hydro_codeine) {
		this.hydro_codeine = hydro_codeine;
	}
	public String getBenzodiazepine() {
		return benzodiazepine;
	}
	public void setBenzodiazepine(String benzodiazepine) {
		this.benzodiazepine = benzodiazepine;
	}
	public String getMethadone() {
		return methadone;
	}
	public void setMethadone(String methadone) {
		this.methadone = methadone;
	}
	public String getAmphet() {
		return amphet;
	}
	public void setAmphet(String amphet) {
		this.amphet = amphet;
	}
	public String getTramad() {
		return tramad;
	}
	public void setTramad(String tramad) {
		this.tramad = tramad;
	}
	public String getMorphine() {
		return morphine;
	}
	public void setMorphine(String morphine) {
		this.morphine = morphine;
	}
	public String getHighestDrugFound() {
		return highestDrugFound;
	}
	public void setHighestDrugFound(String highestDrugFound) {
		this.highestDrugFound = highestDrugFound;
	}
	@Override
	public String toString() {
		return "DeathPersonDetails [caseNumber=" + caseNumber + ", sex=" + sex + ", race=" + race + ", age=" + age
				+ ", deathCity=" + deathCity + ", deathState=" + deathState + ", cause=" + cause + ", heroin=" + heroin
				+ ", cocaine=" + cocaine + ", fentanyl=" + fentanyl + ", oxycodone=" + oxycodone + ", oxymorphone="
				+ oxymorphone + ", etOH=" + etOH + ", hydro_codeine=" + hydro_codeine + ", benzodiazepine="
				+ benzodiazepine + ", methadone=" + methadone + ", amphet=" + amphet + ", tramad=" + tramad
				+ ", morphine=" + morphine + ", highestDrugFound=" + highestDrugFound + "]";
	}
	
	
}
