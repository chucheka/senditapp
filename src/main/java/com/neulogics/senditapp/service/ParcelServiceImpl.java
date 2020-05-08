package com.neulogics.senditapp.service;



import java.util.List;
import java.util.Optional;

import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neulogics.senditapp.exception.ActionNotAllowedException;
import com.neulogics.senditapp.exception.ParcelNotFoundException;
import com.neulogics.senditapp.exception.UserNotFoundException;
import com.neulogics.senditapp.models.Parcel;
import com.neulogics.senditapp.models.User;
import com.neulogics.senditapp.respository.ParcelRepository;
import com.neulogics.senditapp.respository.UserRepository;
import com.neulogics.senditapp.utils.UserUtil;

@Service
public class ParcelServiceImpl {

	@Autowired
	private ParcelRepository repository;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserUtil userUtil;
	
	@Autowired
	private EmailService emailService;

	// GET/api/v1/parcels
	
		public List<Parcel> getAllParcels() throws ParcelNotFoundException, Exception{
			List<Parcel> allParcels;
			try {
				allParcels = repository.findAll();
				if(allParcels.isEmpty()) {
					throw new ParcelNotFoundException("No parcels Found");
				}
				return allParcels;
			}catch(Exception exc) {
				throw new Exception(exc.getMessage());
			}
		
		}
		
		//	GET/api/v1/parcels/{parcelId}
		
		public Parcel getParceById(long parcelId) throws ParcelNotFoundException, Exception{
			
			Optional<Parcel> parcel = repository.findById(parcelId);
			
			if(parcel.isPresent()) {
				return parcel.get();
			}else {
				throw new ParcelNotFoundException("Parcel with parcel Id "+ parcelId + " Not found");
			}
 }
		
		// POST/api/v1/parcels
		
public Parcel createParcelOrder(Parcel newParcel,HttpServletRequest req) throws Exception {
		
			User user =  userUtil.getCurrentUser(req);
			 newParcel.setUser(user);
			 newParcel.setPresentLocation(newParcel.getPickupLocation());
			
			try {
				Parcel parcel = repository.save(newParcel);
				return parcel;
			}catch(Exception exc) {
				throw new Exception("Internal Server Error \n"+exc.getCause());
			}
 }
		
		// GET/api/v1/users/{userId}/parcels
		
public List<Parcel> getParcelsByUserId(long userId) throws UserNotFoundException,ParcelNotFoundException{
				//Find the user by user id
	User user = userRepo.findById(userId).orElseThrow(()->new UserNotFoundException("User does not exist"));
				
	List<Parcel> userParcels = user.getParcels();
	
	if(userParcels.isEmpty()) {
		throw new ParcelNotFoundException("No parcel order(s) for this user");
	}
	
	return userParcels;
 }
		
		// PUT/api/v1/parcels/{parcelId}/status
		
		public Parcel updateParcelStatus(Parcel parcelForUpdate,long parcelId) throws SendFailedException,ActionNotAllowedException,Exception{
			Parcel parcel = repository.findById(parcelId)
					.orElseThrow(()->new ParcelNotFoundException("Parcel to update not found!!"));
			if("delivered".equalsIgnoreCase(parcel.getStatus())) {
				throw new ActionNotAllowedException("Cannot changed status of parcel already delivered");
			}
			// Update the status
            parcel.setStatus(parcelForUpdate.getStatus());
            
            Parcel updatedParcel = repository.save(parcel);
            
            if(updatedParcel != null) {
            	
            	User user = updatedParcel.getUser();
                emailService.sendEmail(user.getEmail(), "UPDATES FROM SENDIT COURIER", "Parcel Status: " +updatedParcel.getStatus());
            }
			return updatedParcel;
 }
		
		// PUT/api/v1/parcels/{parcelId}/cancel
		
		public Parcel cancelParcelOrder( long parcelId, HttpServletRequest req) throws ActionNotAllowedException, Exception  {
			Parcel dbParcel = repository.findById(parcelId)
					.orElseThrow(()->new ParcelNotFoundException("Parcel to cancel not found!!"));
			User authUser = userUtil.getCurrentUser(req);
			User parcelOwner = dbParcel.getUser();
			
			if(!parcelOwner.equals(authUser)) {
				throw new ActionNotAllowedException("Only user who created parcel order can cancel order");
		}
			
			if("delivered".equals(dbParcel.getStatus())) {
				throw new ActionNotAllowedException("Can not cancel parcel order that has been delivered");
			}else {
				
				try {
					dbParcel.setStatus("cancelled");
					return repository.save(dbParcel);
					
				}catch(Exception exc) {
					throw new Exception("Internal Server Error \n"+exc.getCause());
				}
		}
		
 }
	
	// PUT/api/v1/parcels/{parcelId}present_location
		
	public Parcel updateParcelLocation(Parcel parcelForUpdate,long parcelId) throws ParcelNotFoundException,Exception{
		try {
			//Fetch the parcel with particular Id
			Optional<Parcel> parcel = repository.findById(parcelId);
			//Check if parcel exist
			if(parcel.isPresent()) 
	        {
	            Parcel updatedParcel = parcel.get();
	            updatedParcel.setPresentLocation(parcelForUpdate.getPresentLocation());
	            updatedParcel = repository.save(updatedParcel);
	            return updatedParcel;
	        } else {
	        	throw new ParcelNotFoundException("Parcel to change location doen not exist");
	        }
		}catch(Exception exc) {
			throw new Exception("Internal Server Error \n"+exc.getCause());
		}
		
	}
	
	
	//PUT/api/v1/parcels/{parcelId}/destination
	
	public Parcel updateParcelDestination(Parcel parcelForUpdate,long parcelId) throws SendFailedException,Exception{
		
		//Fetch the parcel with particular Id
		Optional<Parcel> parcel = repository.findById(parcelId);
		//Check if parcel exist
		try {
			if(parcel.isPresent()) 
	        {
	            Parcel updatedParcel = parcel.get();
	            if("delivered".equalsIgnoreCase(updatedParcel.getStatus())) {
	            	throw new ActionNotAllowedException("Cannot change destination of already delivered parcel");
	            }
	            // Update the parcel destination
	            updatedParcel.setDestination(parcelForUpdate.getDestination());
	            updatedParcel = repository.save(updatedParcel);
	            if(updatedParcel != null) {
	            	emailService.sendEmail("ryanucheka@gmail.com", "UPDATES FROM SENDIT COURIER", "Parcel currently at  " +updatedParcel.getDestination());
	            }
	            return updatedParcel;
	        } else {
	        	throw new ParcelNotFoundException("Parcel to change destination doen not exist");
	        }
			
		}catch(Exception exc) {
			throw new Exception(exc.getMessage());
		}
		
	}
}
