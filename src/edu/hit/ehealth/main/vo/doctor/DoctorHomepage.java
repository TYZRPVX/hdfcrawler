package edu.hit.ehealth.main.vo.doctor;

import edu.hit.ehealth.main.vo.AbstractVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class DoctorHomepage extends AbstractVO {

    private String personInfoID;
    private String openTime;
    private Integer scanCount;
    private Integer heartGiftNum;
    private Integer thankLetterNum;
    private String patientVoteContent;
    private Integer voteNum;
    private Integer patientReportNum;
    private Integer allPatientNum;
    private Integer followPatientNum;
    private String doctorName;
    private String docTitle;
    private String scienceTitle;
    private Integer heartValue;
    private Integer contriValue;
    private String officeName;
    private String hospitalID;
    private String skills;
    private String doctorIntro;
    private String newYearWord;
    private String consultScope;
    private Integer serviceStarLevel;
    private String serviceStarValue;
    private String rank;
    private Integer visitCount;
    private Integer essayNum;
    private Integer weChatReportNum;


    @Column(name = "grxxid")
    public String getPersonInfoID() {
        return personInfoID;
    }

    public void setPersonInfoID(String personInfoID) {
        this.personInfoID = personInfoID;
    }

    @Column(name = "opentime")
    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    @Column(name = "smcs")
    public Integer getScanCount() {
        return scanCount;
    }

    public void setScanCount(Integer scanCount) {
        this.scanCount = scanCount;
    }

    @Column(name = "xylws")
    public Integer getHeartGiftNum() {
        return heartGiftNum;
    }

    public void setHeartGiftNum(Integer heartGiftNum) {
        this.heartGiftNum = heartGiftNum;
    }

    @Column(name = "gxxs")
    public Integer getThankLetterNum() {
        return thankLetterNum;
    }

    public void setThankLetterNum(Integer thankLetterNum) {
        this.thankLetterNum = thankLetterNum;
    }

    @Column(name = "hztp", columnDefinition = "TEXT")
    public String getPatientVoteContent() {
        return patientVoteContent;
    }

    public void setPatientVoteContent(String patientVoteContent) {
        this.patientVoteContent = patientVoteContent;
    }

    @Column(name = "tps")
    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    @Column(name = "zhbd")
    public Integer getPatientReportNum() {
        return patientReportNum;
    }

    public void setPatientReportNum(Integer patientReportNum) {
        this.patientReportNum = patientReportNum;
    }

    @Column(name = "zhz")
    public Integer getAllPatientNum() {
        return allPatientNum;
    }

    public void setAllPatientNum(Integer allPatientNum) {
        this.allPatientNum = allPatientNum;
    }

    @Column(name = "zsfs")
    public Integer getFollowPatientNum() {
        return followPatientNum;
    }

    public void setFollowPatientNum(Integer followPatientNum) {
        this.followPatientNum = followPatientNum;
    }

    @Column(name = "name")
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Column(name = "doctitle")
    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    @Column(name = "acdtitle")
    public String getScienceTitle() {
        return scienceTitle;
    }

    public void setScienceTitle(String scienceTitle) {
        this.scienceTitle = scienceTitle;
    }

    @Column(name = "axz")
    public Integer getHeartValue() {
        return heartValue;
    }

    public void setHeartValue(Integer heartValue) {
        this.heartValue = heartValue;
    }

    @Column(name = "gxz")
    public Integer getContriValue() {
        return contriValue;
    }

    public void setContriValue(Integer contriValue) {
        this.contriValue = contriValue;
    }

    @Column(name = "ks")
    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    @Column(name = "yyid")
    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    @Column(name = "sc", columnDefinition = "TEXT")
    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    @Column(name = "jj", columnDefinition = "TEXT")
    public String getDoctorIntro() {
        return doctorIntro;
    }

    public void setDoctorIntro(String doctorIntro) {
        this.doctorIntro = doctorIntro;
    }

    @Column(name = "xnxy", columnDefinition = "TEXT")
    public String getNewYearWord() {
        return newYearWord;
    }

    public void setNewYearWord(String word) {
        this.newYearWord = word;
    }

    @Column(name = "zxfw", columnDefinition = "TEXT")
    public String getConsultScope() {
        return consultScope;
    }

    public void setConsultScope(String consultScope) {
        this.consultScope = consultScope;
    }

    @Column(name = "fwxdj")
    public Integer getServiceStarLevel() {
        return serviceStarLevel;
    }

    public void setServiceStarLevel(Integer serviceStarLevel) {
        this.serviceStarLevel = serviceStarLevel;
    }

    @Column(name = "fwxz")
    public String getServiceStarValue() {
        return serviceStarValue;
    }

    public void setServiceStarValue(String serviceStarValue) {
        this.serviceStarValue = serviceStarValue;
    }

    @Column(name = "pm")
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Column(name = "zfw")
    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    @Column(name = "zwz")
    public Integer getEssayNum() {
        return essayNum;
    }

    public void setEssayNum(Integer essayNum) {
        this.essayNum = essayNum;
    }

    @Column(name = "wxzhbd")
    public Integer getWeChatReportNum() {
        return weChatReportNum;
    }

    public void setWeChatReportNum(Integer weChatReportNum) {
        this.weChatReportNum = weChatReportNum;
    }

}
