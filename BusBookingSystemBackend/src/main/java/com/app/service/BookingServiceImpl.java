package com.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.app.dao.BookingsDao;
import com.app.dao.BusDao;
import com.app.dao.PassengerDao;
import com.app.dao.RouteDao;
import com.app.dao.SeatAllocationDao;
import com.app.dao.StationDao;
import com.app.dao.UserDao;
import com.app.dto.ApiResponse;
import com.app.dto.BookingDetailsDto;
import com.app.dto.BookingsDto;
import com.app.dto.GetBookingDto;
import com.app.dto.GetBookings;
import com.app.dto.PassengerDto;
import com.app.dto.SeatPassengerDto;
import com.app.entities.Bookings;
import com.app.entities.Bus;
import com.app.entities.Passenger;
import com.app.entities.SeatAllocation;
import com.app.entities.User;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingsDao bookingDao;

	@Autowired
	private PassengerDao passengerDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RouteDao routeDao;

	@Autowired
	private StationDao stationDao;

	@Autowired
	private BusDao busDao;

	@Autowired
	private SeatAllocationDao seatAllocationDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ApiResponse addBooking(BookingsDto booking) {
		try {

			// Retrieve user and bus entities
			User user = userDao.findById(booking.getUserId()).orElseThrow(() -> new RuntimeException("User Not found"));
			Bus bus = busDao.findById(booking.getBusId()).orElseThrow(() -> new RuntimeException("Bus Not Found"));

			// Create a new booking entity
			Bookings newBooking = new Bookings();
			
			newBooking.setPaymentId(booking.getPaymentId());
			newBooking.setRazorpayOrderId(booking.getRazorpayOrderId());
			newBooking.setRazorpaySignature(booking.getRazorpaySignature());
			newBooking.setBookingDateTime(LocalDateTime.now());
			newBooking.setUser(user);
			newBooking.setBus(bus);
			newBooking.setFare(booking.getFare());

			// Iterate through seat-passenger list to save passengers and validate seat
			// allocations
			for (SeatPassengerDto seatPassenger : booking.getSeatPassengerList()) {
				int seatNo = seatPassenger.getSeatNo();
				// Check if the seat is already allocated
				boolean isSeatAllocated = seatAllocationDao.existsByBusAndSeatNo(bus, seatNo);
				if (isSeatAllocated) {
					return new ApiResponse("Seat " + seatNo + " is already allocated.", HttpStatus.BAD_REQUEST);
				}
				// Map DTO to entity for passenger
				Passenger passenger = mapDtoToEntity(seatPassenger.getPassenger());
				// Save the passenger entity
				Passenger savedPassenger = passengerDao.save(passenger);

				// Create seat allocation for the booking
				SeatAllocation seatAllocation = new SeatAllocation();
				seatAllocation.setSeatNo(seatNo);
				seatAllocation.setPassenger(savedPassenger);
				seatAllocation.setBooking(newBooking);
				seatAllocation.setBus(bus);
				// Add the seat allocation to the booking
				newBooking.addSeat(seatAllocation);
			}

			// Save the new booking entity
			Bookings savedBooking = bookingDao.save(newBooking);

			// Check if the booking was successfully saved
			if (savedBooking != null) {
				return new ApiResponse("Booking Successful.", HttpStatus.CREATED);
			} else {
				return new ApiResponse("Failed to add booking.", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ObjectOptimisticLockingFailureException ex) {
			// Handle optimistic locking failure
			return new ApiResponse(
					"Optimistic locking failure: Another user has updated the booking. Please try again.",
					HttpStatus.CONFLICT);

		}
	}

	@Override
	public List<GetBookingDto> getAllUserBookings(long userid) throws RuntimeException {
		User user = userDao.findById(userid).orElseThrow(() -> new RuntimeException("User Not Found"));
		List<Bookings> bookinglist = bookingDao.findByUser(user)
				.orElseThrow(() -> new RuntimeException("No Bookings found"));

		List<GetBookingDto> bookedDtoList = new ArrayList<GetBookingDto>();

		for (Bookings booking : bookinglist) {

			Bus bus = booking.getBus();
			GetBookingDto bookingDto = new GetBookingDto();
			bookingDto.setId(booking.getId());
			bookingDto.setBusNo(bus.getBusNo());
			bookingDto.setFrom(bus.getRoute().getStationIdBoarding().getStationName());
			bookingDto.setTo(bus.getRoute().getStationIdDestination().getStationName());

			bookingDto.setStartTime(bus.getStartTime());
			bookingDto.setEndTime(bus.getEndTime());
			bookingDto.setTotalFare(booking.getFare());
			bookingDto.setBookingDateTime(booking.getBookingDateTime());
			bookedDtoList.add(bookingDto);
		}
		return bookedDtoList;
	}

	@Override
	public BookingDetailsDto getBookingDetails(long bookingId) {
		// Retrieve booking entity by ID
		Optional<Bookings> bookingOptional = bookingDao.findById(bookingId);

		// Check if the booking exists
		if (bookingOptional.isPresent()) {
			// Map booking entity to BookingDetailsDto
			Bookings booking = bookingOptional.get();
			BookingDetailsDto bookingDto = new BookingDetailsDto();
			bookingDto.setBusNo(booking.getBus().getBusNo());
			bookingDto.setFrom(booking.getBus().getRoute().getStationIdBoarding().getStationName());
			bookingDto.setTo(booking.getBus().getRoute().getStationIdDestination().getStationName());
			bookingDto.setStartTime(booking.getBus().getStartTime());
			bookingDto.setEndTime(booking.getBus().getEndTime());
			bookingDto.setTotalFare(booking.getFare());
			bookingDto.setBookingDateTime(booking.getBookingDateTime());

			// Map seat-passenger list to SeatPassengerDto list
			List<SeatAllocation> seatList = booking.getSeatList();
			List<SeatPassengerDto> seatPassengerList = seatList.stream().map(seatAllocation -> {
				SeatPassengerDto seatPassengerDto = new SeatPassengerDto();
				seatPassengerDto.setSeatNo(seatAllocation.getSeatNo());
				seatPassengerDto.setPassenger(mapEntityToDto(seatAllocation.getPassenger()));
				return seatPassengerDto;
			}).collect(Collectors.toList());

			bookingDto.setSeatPassengerList(seatPassengerList);

			return bookingDto;
		} else {
			return null; // Booking not found
		}
	}

	private PassengerDto mapEntityToDto(Passenger passenger) {
		PassengerDto passengerDto = new PassengerDto();
		passengerDto.setFirstName(passenger.getFirstName());
		passengerDto.setLastName(passenger.getLastName());
		passengerDto.setGender(passenger.getGender());
		passengerDto.setAge(passenger.getAge());
		return passengerDto;
	}

	public Passenger mapDtoToEntity(PassengerDto passengerDto) {
		return modelMapper.map(passengerDto, Passenger.class);
	}


	
	
	 public List<GetBookings> getAllBookings() {
	        List<Bookings> bookings = bookingDao.findAll();
	        return mapToGetBookings(bookings);
	    }

	 public List<GetBookings> mapToGetBookings(List<Bookings> bookings) {
	        return bookings.stream().map(this::mapToGetBooking).collect(Collectors.toList());
	    }

	    private GetBookings mapToGetBooking(Bookings booking) {
	        GetBookings getBookings = new GetBookings();
	        getBookings.setBookingId(booking.getId());
	        getBookings.setPaymentId(booking.getPaymentId());
	        getBookings.setRazorpayOrderId(booking.getRazorpayOrderId());
	        getBookings.setRazorpaySignature(booking.getRazorpaySignature());
	        getBookings.setBusNo(booking.getBus().getBusNo());
	        getBookings.setBusId(booking.getBus().getId());
	        getBookings.setTotalFare(booking.getFare());
	        getBookings.setUserId(booking.getUser().getId());
	        getBookings.setUserName(booking.getUser().getEmail());
	        getBookings.setBookingDateTime(booking.getBookingDateTime());
	        getBookings.setNoOfSeats(booking.getSeatList().size());
	        return getBookings;
	    }
	
	
	
	
	
	@Override
	public ApiResponse cancelBookings(long bookingid) {
		try {
            // Retrieve booking entity by ID
            Optional<Bookings> bookingOptional = bookingDao.findById(bookingid);

            // Check if the booking exists
            if (bookingOptional.isPresent()) {
                Bookings booking = bookingOptional.get();

                // Delete the booking and associated seat allocations
                bookingDao.delete(booking);

                return new ApiResponse("Booking deleted successfully.", HttpStatus.OK);
            } else {
                return new ApiResponse("Booking not found.", HttpStatus.NOT_FOUND);
            }
        } catch (ObjectOptimisticLockingFailureException ex) {
            // Handle optimistic locking failure
            return new ApiResponse(
                    "Optimistic locking failure: Another user has updated the booking. Please try again.",
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ApiResponse("Failed to delete booking.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}