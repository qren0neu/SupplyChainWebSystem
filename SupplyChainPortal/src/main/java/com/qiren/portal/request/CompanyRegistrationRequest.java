package com.qiren.portal.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CompanyRegistrationRequest {

	@NotBlank(message = "Please enter your companty name")
	@Pattern(regexp = "[A-Za-z]+", message = "Invalid name format")
	private String name;
	@NotBlank(message = "Please enter your address")
	@Pattern(regexp = "([a-zA-Z0-9]+\\s?)+", message = "Invalid address format")
	private String address;
	@NotBlank(message = "Please enter your city")
	@Pattern(regexp = "[A-Za-z]+", message = "Invalid city format")
	private String city;
	@NotBlank(message = "Please enter your state")
	@Pattern(regexp = "[A-Za-z]+{2}", message = "Invalid state format")
	private String state;
	@NotBlank(message = "Please enter your contry")
	@Pattern(regexp = "[A-Za-z]+", message = "Invalid contry format")
	private String country;
	@NotBlank(message = "Please enter your phone number")
	@Pattern(regexp = "(\\(\\+[0-9]+\\))?\\s?[0-9]{3}-[0-9]{3}-[0-9]{4}", message = "Invalid phone number format")
	private String phone;
	@NotBlank(message = "Please enter your email")
	@Email(message = "Invalid email format")
	private String email;
	@NotBlank(message = "Please select your role")
	private String role;
}
