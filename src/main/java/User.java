import java.lang.reflect.Field;

public class User{
    private int userId;
    private String userName;
    private char userGender;
    private long mobileNo;

    public User() {

    }

    public User(int userId, String userName, char userGender, long mobileNo) {
        this.userId = userId;
        this.userName = userName;
        this.userGender = userGender;
        this.mobileNo = mobileNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public char getUserGender() {
        return userGender;
    }

    public void setUserGender(char userGender) {
        this.userGender = userGender;
    }

    public long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String findValue(String searchField) throws IllegalAccessException {
        Field[] fields = User.class.getDeclaredFields();
        for(Field f : fields){
            if(searchField == f.getName()) {
                return f.get(this).toString();
            }
        }
        return null;
    }

}
