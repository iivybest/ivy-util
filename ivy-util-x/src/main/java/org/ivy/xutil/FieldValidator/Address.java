package org.ivy.xutil.FieldValidator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Address {
 
	@NotBlank(message = "linel1 is blank")
    private String line1;

    @Pattern(regexp="^\\d{4}-\\d{2}-\\d{2}$", message="日期格式错误，YYYY-MM-DD")
    private String line2;
    
    private String zip;
    @Status(message="state 只能取值0，1")
    private String state;
     
    @Length(max = 20)
    @NotNull
    private String country;
 
    @Range(min = 2, max = 50, message = "Floor out of range")
    public int floor;

	/**
	 * @return the line1
	 */
	public String getLine1() {
		return line1;
	}

	/**
	 * @param line1 the line1 to set
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}

	/**
	 * @return the line2
	 */
	public String getLine2() {
		return line2;
	}

	/**
	 * @param line2 the line2 to set
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the floor
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * @param floor the floor to set
	 */
	public void setFloor(int floor) {
		this.floor = floor;
	}
    
	public static void main(String[] args) throws FieldValidateException {
		Address addr = new Address();
		addr.setLine2("0698-12-01");
		HiberanteValidatorUtil.validate(addr);
	}
 
}