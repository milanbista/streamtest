package com.example.streamtest.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.streamtest.model.Customer;
import com.example.streamtest.repos.CustomerRepo;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
@RequestMapping("/api/v1")
public class StreamController {

	@Autowired
	private CustomerRepo customerRepo;
	
	@RequestMapping(path = "/name", method = RequestMethod.GET)
	public String getName() {
		
		Optional<Customer> c =  customerRepo.findById(1);
		
		return c.get().getName();
	}
	
	@RequestMapping(path = "/names", method = RequestMethod.GET)
	public String getNames() {
		
		return "hello:";
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping(value="/convert")
	public ResponseEntity<InputStreamResource> convertToPdf(@RequestParam("image") MultipartFile image) throws IOException{

	
	   return  this.convertPDF(image);    
	
}

	private ResponseEntity<InputStreamResource> convertPDF (MultipartFile jpegData) {
		
		
		
		try {
			
			BufferedImage image = ImageIO.read(jpegData.getInputStream());
			
			float imageWidth = image.getWidth();
			float imageHeight = image.getHeight();
			
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);
			
			PDImageXObject image1 = PDImageXObject.createFromByteArray(document, jpegData.getBytes(), jpegData.getName());
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
//			contentStream.drawImage(image1, 0,0,imageWidth, imageHeight);
			contentStream.drawImage(image1,0, 0,1000,1000);
			contentStream.close();
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			document.save(byteArrayOutputStream);
			document.close();
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "output.pdf");
			
			ByteArrayInputStream a = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			
			return new ResponseEntity<InputStreamResource>(new InputStreamResource(a),headers, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		

	}
	
	
}

