package Project_Classes;

public class DataClass {
    // PERSONAL INFORMATION FIELDS
    private static String fullName;
    private static String NICNumber;
    private static String phoneNumber;
    private static String gmail;
    private static String dateOfBirth;
    private static String gender;
    private static String maritalStatus;
    private static String address;
    private static String city;
    private static String password;
    private static String filerStatus;

    // Father's Details
    private static String fatherName;
    private static String fatherNICNumber;
    private static String fatherPhoneNumber;

    //  ACCOUNT INFORMATION FIELDS
    private static String accountNumber;
    private static String accountType;
    private static String religion;
    private static String accountPurpose;
    private static String occupation;
    private static String monthlyIncome;
    private static String qualification;
    private static String currencyType;
    private static String cardType;
    private static String additionalServices;
    private static String accountPin;
    private static String smsAlerts;
    private static String emailAlerts;

    private static double balance = 0.00;
    private static double filerTax = 0.00;

    public static String getFullName() { return fullName; }
    public static void setFullName(String fullName) { DataClass.fullName = fullName; }

    public static String getNICNumber() { return NICNumber; }
    public static void setNICNumber(String NICNumber) { DataClass.NICNumber = NICNumber; }

    public static String getPhoneNumber() { return phoneNumber; }
    public static void setPhoneNumber(String phoneNumber) { DataClass.phoneNumber = phoneNumber; }

    public static String getGmail() { return gmail; }
    public static void setGmail(String gmail) { DataClass.gmail = gmail; }

    public static String getPassword() { return password; }
    public static void setPassword(String password) { DataClass.password = password; }

    public static String getDateOfBirth() { return dateOfBirth; }
    public static void setDateOfBirth(String dateOfBirth) { DataClass.dateOfBirth = dateOfBirth; }

    public static String getGender() { return gender; }
    public static void setGender(String gender) { DataClass.gender = gender; }

    public static String getMaritalStatus() { return maritalStatus; }
    public static void setMaritalStatus(String maritalStatus) { DataClass.maritalStatus = maritalStatus; }

    public static String getFilerStatus() { return filerStatus; }
    public static void setFilerStatus(String filerStatus) { DataClass.filerStatus = filerStatus; }

    public static String getAddress() { return address; }
    public static void setAddress(String address) { DataClass.address = address; }

    public static String getCity() { return city; }
    public static void setCity(String city) { DataClass.city = city; }

    public static String getFatherName() { return fatherName; }
    public static void setFatherName(String fatherName) { DataClass.fatherName = fatherName; }

    public static String getFatherNICNumber() { return fatherNICNumber; }
    public static void setFatherNICNumber(String fatherNICNumber) { DataClass.fatherNICNumber = fatherNICNumber; }

    public static String getFatherPhoneNumber() { return fatherPhoneNumber; }
    public static void setFatherPhoneNumber(String fatherPhoneNumber) { DataClass.fatherPhoneNumber = fatherPhoneNumber; }

    public static String getAccountNumber() { return accountNumber; }
    public static void setAccountNumber(String accountNumber) { DataClass.accountNumber = accountNumber; }

    public static String getAccountPin() { return accountPin; }
    public static void setAccountPin(String accountPin) { DataClass.accountPin = accountPin; }

    public static String getAccountType() { return accountType; }
    public static void setAccountType(String accountType) { DataClass.accountType = accountType; }

    public static String getReligion() { return religion; }
    public static void setReligion(String religion) { DataClass.religion = religion; }

    public static String getAccountPurpose() { return accountPurpose; }
    public static void setAccountPurpose(String accountPurpose) { DataClass.accountPurpose = accountPurpose; }

    public static String getOccupation() { return occupation; }
    public static void setOccupation(String occupation) { DataClass.occupation = occupation; }

    public static String getMonthlyIncome() { return monthlyIncome; }
    public static void setMonthlyIncome(String monthlyIncome) { DataClass.monthlyIncome = monthlyIncome; }

    public static String getQualification() { return qualification; }
    public static void setQualification(String qualification) { DataClass.qualification = qualification; }

    public static String getCurrencyType() { return currencyType; }
    public static void setCurrencyType(String currencyType) { DataClass.currencyType = currencyType; }

    public static String getCardType() { return cardType; }
    public static void setCardType(String cardType) { DataClass.cardType = cardType; }

    public static String getAdditionalServices() { return additionalServices; }
    public static void setAdditionalServices(String additionalServices) { DataClass.additionalServices = additionalServices; }

    public static String getSmsAlerts() { return smsAlerts; }
    public static void setSmsAlerts(String smsAlerts) { DataClass.smsAlerts = smsAlerts; }

    public static String getEmailAlerts() { return emailAlerts; }
    public static void setEmailAlerts(String emailAlerts) { DataClass.emailAlerts = emailAlerts; }

    public static double getBalance() { return balance; }
    public static void setBalance(double balance) { DataClass.balance = balance; }

    public static double getFilerTax() { return filerTax; }
    public static void setFilerTax(double filerTax) { DataClass.filerTax = filerTax; }
}