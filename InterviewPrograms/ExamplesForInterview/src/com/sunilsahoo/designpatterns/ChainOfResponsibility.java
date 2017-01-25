package com.sunilsahoo.designpatterns;

/**
 * Type: Behavioural ## Intent Avoid coupling the sender of a request to its
 * receiver by giving more than one object a chance to handle the request. Chain
 * the receiving objects and pass the request along the chain until an object
 * handles it.
 * 
 * ## Applicability Use Chain of Responsibility when
 * 
 * more than one object may handle a request, and the handler isn't known a
 * priori. The handler should be ascertained automatically you want to issue a
 * request to one of several objects without specifying the receiver explicitly
 * the set of objects that can handle a request should be specified dynamically
 * 
 * Avoid coupling the sender of a request to its receiver by giving more than
 * one object a chance to handle the request. Chain the receiving objects and
 * pass the request along the chain until an object handles it.
 * 
 * @author sunilkumarsahoo
 *
 */
public class ChainOfResponsibility {
	public static void main(String[] args) {
		IFileValidator audioValidator = new AudioValidator();
		IFileValidator videoValidator = new VideoValidator();
		IFileValidator imageValidator = new ImageValidator();
		audioValidator.setNextValidator(videoValidator);
		videoValidator.setNextValidator(imageValidator);

		String fileName = "vandemataram.pdf";
		audioValidator.validate(fileName);
	}

}

interface IFileValidator {
	void validate(String filname);

	void setNextValidator(IFileValidator fileValidator);
}

class AudioValidator implements IFileValidator {
	IFileValidator fileValidator;

	@Override
	public void setNextValidator(IFileValidator fileValidator) {
		this.fileValidator = fileValidator;
	}

	@Override
	public void validate(String filname) {
		if (filname.endsWith("mp3")) {
			System.out.println(filname + " is Mp3 file");
			fileValidator = null;
		}
		if (fileValidator != null) {
			fileValidator.validate(filname);
		}
	}

}

class VideoValidator implements IFileValidator {
	IFileValidator fileValidator;

	@Override
	public void setNextValidator(IFileValidator fileValidator) {
		this.fileValidator = fileValidator;
	}

	@Override
	public void validate(String filname) {
		if (filname.endsWith("mp4")) {
			System.out.println(filname + " is a Vudio file");
			fileValidator = null;
		}
		if (fileValidator != null) {
			fileValidator.validate(filname);
		}
	}

}

class ImageValidator implements IFileValidator {
	IFileValidator fileValidator;

	@Override
	public void setNextValidator(IFileValidator fileValidator) {
		this.fileValidator = fileValidator;
	}

	@Override
	public void validate(String filname) {
		if (filname.endsWith("jpg")) {
			System.out.println(filname + " is an Image file");
			fileValidator = null;
		}
		if (fileValidator != null) {
			fileValidator.validate(filname);
		} else {
			System.out.println(filname + " is not supported");
		}
	}

}
