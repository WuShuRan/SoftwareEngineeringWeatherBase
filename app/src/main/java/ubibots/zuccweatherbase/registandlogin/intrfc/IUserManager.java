package ubibots.zuccweatherbase.registandlogin.intrfc;

public interface IUserManager {

    String login(String userName, String userPassword);
    String regist(String userName, String userMail, String userPassword, String userPassword2);
    String remove(String userName);
}
