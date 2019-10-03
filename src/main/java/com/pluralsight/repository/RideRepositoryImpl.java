package com.pluralsight.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;
import com.pluralsight.repository.util.RideRowMapper;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Ride> getRides() {
		List<Ride> rides = jdbcTemplate.query("select * from ride", new RideRowMapper());
		return rides;
	}

	@Override
	public Ride createRide(Ride ride) {
		/*
		 * KeyHolder keyHolder = new GeneratedKeyHolder(); jdbcTemplate.update(new
		 * PreparedStatementCreator() {
		 * 
		 * @Override public PreparedStatement createPreparedStatement(Connection con)
		 * throws SQLException { // TODO Auto-generated method stub
		 * 
		 * 
		 * PreparedStatement ps =
		 * con.prepareStatement("insert into ride (name, duration) values (?,?)", new
		 * String[] {"id"}); ps.setString(1, ride.getName()); ps.setInt(2,
		 * ride.getDuration()); return ps; } }, keyHolder);
		 * 
		 * Number id = keyHolder.getKey();
		 */
		
		 //ALTERNATIVE -  TO SAVE THE DATA INTO THE DATABASE AND GET THE KEY
		 //--Create an List that will match to the column in the DB
		 SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		 List<String> columns = new ArrayList<>();
		 
		 columns.add("name");
		 columns.add("duration");
		 
		 //--Create object that we will put into the databasae 
		 Map<String, Object>data = new HashMap<>(); 
		 data.put("name", ride.getName());
		 data.put("duration", ride.getDuration());
		 
		 insert.setGeneratedKeyName("id"); 
		 insert.setTableName("ride");
		 insert.setColumnNames(columns); 
		 Number id = insert.executeAndReturnKey(data);
		 
		 System.out.println(id);
		
		return getRide(id.intValue());
	}
	
	@Override
	public Ride getRide(Integer id) {
		Ride ride = jdbcTemplate.queryForObject(
				"select * from ride where id = ?", new RideRowMapper(), id);
		return ride;
	}
	
	/*//SimpleJdbc Insert - TO SAVE THE DATA INTO THE DATABASE
	 * @Override public Ride createRide(Ride ride) {
	 * //SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
	 * 
	 * //--Create an List that will match to the column in the DB
	 * 
	 * List<String> columns = new ArrayList<>(); columns.add("name");
	 * columns.add("duration");
	 * 
	 * //--Create object that we will put into the databasae Map<String, Object>
	 * data = new HashMap<>(); data.put("name", ride.getName());
	 * data.put("duration", ride.getDuration());
	 * 
	 * insert.setGeneratedKeyName("id"); insert.setTableName("ride");
	 * insert.setColumnNames(columns); Number key =
	 * insert.executeAndReturnKey(data);
	 * 
	 * System.out.println(key);
	 * 
	 * return null; }
	 */
	
	@Override
	public Ride updateRide(Ride ride) {
		jdbcTemplate.update("update ride set name = ?, duration = ? where id = ?", 
				ride.getName(), ride.getDuration(), ride.getId());
		return ride;
	}
	
	@Override
	public void updateRides(List<Object[]> pairs) {
		jdbcTemplate.batchUpdate("update ride set ride_date = ? where id = ?", pairs);
		
		
	}
	
	
	
}
