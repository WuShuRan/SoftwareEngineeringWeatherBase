package ubibots.zuccweatherbase.registandlogin.util;

public class DbException extends BaseException {
	public DbException(Throwable ex){
		super("Database error: "+ex.getMessage());
	}
}
