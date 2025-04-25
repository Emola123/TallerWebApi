package com.unimag.proyect.repositories;

import com.unimag.proyect.entities.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void shouldSavePatient() {
        Patient patient = Patient.builder()
                .fullName("Ana Gómez")
                .email("ana.gomez@example.com")
                .phone("123456789")
                .build();

        Patient saved = patientRepository.save(patient);

        assertNotNull(saved.getId());
        assertEquals("Ana Gómez", saved.getFullName());
        assertEquals("ana.gomez@example.com", saved.getEmail());
        assertEquals("123456789", saved.getPhone());
    }

    @Test
    void shouldFindPatientById() {
        Patient patient = patientRepository.save(Patient.builder()
                .fullName("Carlos Pérez")
                .email("carlos.perez@example.com")
                .phone("987654321")
                .build());

        Patient found = patientRepository.findById(patient.getId()).orElseThrow();

        assertEquals(patient.getId(), found.getId());
        assertEquals("Carlos Pérez", found.getFullName());
        assertEquals("carlos.perez@example.com", found.getEmail());
        assertEquals("987654321", found.getPhone());
    }

    @Test
    void shouldDeletePatient() {
        Patient patient = patientRepository.save(Patient.builder()
                .fullName("Laura Rodríguez")
                .email("laura.rodriguez@example.com")
                .phone("123123123")
                .build());

        Long id = patient.getId();

        patientRepository.deleteById(id);

        assertFalse(patientRepository.findById(id).isPresent());
    }

    @Test
    void shouldFindPatientByEmail() {
        Patient patient = patientRepository.save(Patient.builder()
                .fullName("José Martínez")
                .email("jose.martinez@example.com")
                .phone("321321321")
                .build());

        Patient found = patientRepository.findById(patient.getId()).orElseThrow();

        assertEquals("jose.martinez@example.com", found.getEmail());
    }
}
