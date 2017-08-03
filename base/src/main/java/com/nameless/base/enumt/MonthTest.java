package com.nameless.base.enumt;

public class MonthTest {
	
	public static void main(String[] args) {
		System.out.println(Months.M201701.name() + Months.M201701.getClass());
		System.out.println(Months.M201702.ordinal());
		System.out.println(Months.valueOf("M201705"));
		System.out.println(Months.values());
		
		System.out.println(String.format("name=%s code=%s namecn=%s nameen=%s", 
				Months.M201701.name(),Months.M201701.getCode(),Months.M201701.getNameCn(),Months.M201701.getNameEn()));
	}
	
}

enum Months {

	M201701("201701","2017年01月","2017-02"),
	M201702("201702","2017年02月","2017-03"),
	M201703("201703","2017年03月","2017-04"),
	M201704("201704","2017年04月","2017-05"),
	M201705("201705","2017年05月","2017-06"),
	M201706("201706","2017年06月","2017-07"),
	M201707("201707","2017年07月","2017-08"),
	M201708("201708","2017年08月","2017-09"),
	M201709("201709","2017年09月","2017-10"),
	M201710("201710","2017年10月","2017-11"),
	M201711("201711","2017年11月","2017-12"),
	M201712("201712","2017年12月","2017-13");
	
	public String getCode() {
		return code;
	}

	public String getNameCn() {
		return nameCn;
	}

	public String getNameEn() {
		return nameEn;
	}

	private String code;
	private String nameCn;
	private String nameEn;
	
	private Months(String code , String nameCn , String nameEn){
		this.code = code;
		this.nameCn = nameCn;
		this.nameEn = nameEn;
	}
	
}