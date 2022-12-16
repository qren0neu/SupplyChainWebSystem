package com.qiren.portal.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

	@NotBlank(message = "Please enter your first name")
	@Pattern(regexp = "[A-Za-z]+", message = "Invalid name format")
	private String fname;

	// @Pattern(regexp = "(?:[A-Za-z]+|)", message = "Invalid name format")
	private String mname;

	@NotBlank(message = "Please enter your last name")
	@Pattern(regexp = "[A-Za-z]+", message = "Invalid name format")
	private String lname;

	@NotBlank(message = "Please select your gender")
	private String gender;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "Please select a past date as your birthday")
	private Date birthday;

	@NotBlank(message = "Please enter your address")
	@Pattern(regexp = "([a-zA-Z0-9]+\\s?)+", message = "Invalid address format")
	private String address1;
	// @Pattern(regexp = "(?:([a-zA-Z0-9]+\\\\s?)+|)", message = "Invalid address
	// format")
	private String address2;
	@NotBlank(message = "Please enter your email")
	@Email(message = "Invalid email format")
	private String email;
	@NotBlank(message = "Please enter your phone number")
	@Pattern(regexp = "(\\(\\+[0-9]+\\))?\\s?[0-9]{3}-[0-9]{3}-[0-9]{4}", message = "Invalid phone number format")
	private String phone;
	@NotBlank(message = "Please select your preference")
	private String preference;

}
