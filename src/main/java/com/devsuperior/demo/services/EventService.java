package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {
	
	@Autowired
	private EventRepository repository;
	
	@Transactional(readOnly = true)
    public Page<EventDTO> findAllPaged(Pageable pageable) {
        Page<Event> list = repository.findAll(pageable);
        return list.map(x -> new EventDTO(x));
    }
	
	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity.setDate(dto.getDate());
			entity.setUrl(dto.getUrl());
			entity.setCity(new City(dto.getCityId(), null));
			entity = repository.save(entity);
			return new EventDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	@Transactional
	public EventDTO insert(EventDTO dto) {
		Event entity = new Event();
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.setCity(new City(dto.getCityId(), null));
		entity = repository.save(entity);
		return new EventDTO(entity);
	}
	
	
}
