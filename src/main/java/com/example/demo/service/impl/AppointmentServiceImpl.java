package com.example.demo.service.impl;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.exceptionhandler.*;
import com.example.demo.mapper.AppointmentMapper;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;


    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }


//    @Transactional
//    public AppointmentDto createAppointment(AppointmentDto dto) {
//
//
//        // الحالات التي تعتبر تعارضاً
//        List<Appointment.Status> conflictingStatuses = List.of(
//                Appointment.Status.SCHEDULED,
//                Appointment.Status.COMPLETED
//        );
//
//        // تحقق من وجود تعارض سواء للدكتور أو للمريض في نفس الوقت
//        boolean conflictExists = appointmentRepository.existsByDoctorIdOrPatientIdAndAppointmentDateTimeAndStatusIn(
//                dto.getDoctorId(),
//                dto.getPatientId(),
//                dto.getAppointmentDateTime(),
//                conflictingStatuses
//        );
//
//        if (conflictExists) {
//            throw new RuntimeException("Doctor or patient is not available at this time.");
//        }
//
//        // أولاً: تأكد إن المعاد في المستقبل
//        if (dto.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException("Appointment must be in the future");
//        }
//
//        // جلب كائنات الدكتور والمريض من قاعدة البيانات
//        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
//                .orElseThrow(() -> new RuntimeException("Not Found doctor with ID " + dto.getDoctorId()));
//        Patient patient = patientRepository.findById(dto.getPatientId())
//                .orElseThrow(() -> new RuntimeException("Not Found patient with ID " + dto.getPatientId()));
//
//        // إنشاء وحفظ الموعد
//        Appointment appointment = appointmentMapper.toEntity(dto);
//        appointment.setDoctor(doctor);
//        appointment.setPatient(patient);
//        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
//
//        Appointment saved = appointmentRepository.save(appointment);
//        return appointmentMapper.toDto(saved);
//    }


    @Override
    @Transactional
    public AppointmentDto createAppointment(AppointmentDto dto) {

        List<Appointment.Status> conflictingStatuses = List.of(
                Appointment.Status.SCHEDULED,
                Appointment.Status.COMPLETED
        );

        Boolean existing1 = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusIn(dto.getDoctorId(), dto.getAppointmentDateTime(), conflictingStatuses);
        Boolean existing2 = appointmentRepository.existsByPatientIdAndAppointmentDateTimeAndStatusIn(dto.getPatientId(), dto.getAppointmentDateTime(), conflictingStatuses);


        if (existing1) {
            throw new DoctorNotAvaliableException("Doctor is not available at this time or completed.");
        }
        if (existing2) {
            throw new PatientNotAvaliableException("You already have an appointment at this time or completed.");

        }


        if (dto.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment must be in the future");
        }
        Appointment appointment = appointmentMapper.toEntity(dto);
        Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow(() -> new RuntimeException("Not Found doctor with " + dto.getDoctorId()));
        Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow(() -> new RuntimeException("Not Found Patient with " + dto.getPatientId()));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(saved);

    }


    public AppointmentDto update(Long id, AppointmentDto dto) {

        Appointment existing = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Not Found appointment with" + id));
        //2️⃣ لو الـ DTO اللي جاي فيه doctorId جديد:
        //بيروح يجيب الدكتور الجديد من قاعدة البيانات.
        //يبدّل الدكتور القديم بالدكتور الجديد في نفس الـ appointment.

        if (dto.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new DoctorNotFoundException("Not Found doctor with " + dto.getDoctorId()));
            existing.setDoctor(doctor);
        }
        //بيجيب المريض الجديد من قاعدة البيانات.
        //يبدّله مكان المريض القديم في الموعد.

        if (dto.getPatientId() != null) {
            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new PatientNotFoundException("Not Found patient with " + dto.getPatientId()));
            existing.setPatient(patient);
        }

        existing.setAppointmentDateTime(dto.getAppointmentDateTime());


        appointmentMapper.updateAppointmentfromDto(dto, existing);
        Appointment saved = appointmentRepository.save(existing);

        return appointmentMapper.toDto(saved);


    }

    @Transactional
    public AppointmentDto cancelAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Not Found Appointment with " + id));

        if (LocalDateTime.now().isAfter(appointment.getAppointmentDateTime())) {
            throw new CannotCancelPastAppointmentException("cannot cancel a past appointment");
        }
        appointment.setStatus(Appointment.Status.CANCELED);
        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(saved);


    }

    @Transactional
    public AppointmentDto compeleteAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Not Found Appointment with " + id));

        if (LocalDateTime.now().isBefore(appointment.getAppointmentDateTime())) {
            throw new CannotcompleteaappointmentbeforefinishedException("cannot complete a appointment before finished");
        }
        appointment.setStatus(Appointment.Status.COMPLETED);
        return appointmentMapper.toDto(appointmentRepository.saveAndFlush(appointment));


    }


    public List<AppointmentDto> getAllDoctorIdAndAppointment(Long doctorid, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorid, start, end);
        return appointments.stream().map(appointmentMapper::toDto).collect(Collectors.toList());
    }


    public List<AppointmentDto> getAllPatientIdAndAppointment(Long patientid, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndAppointmentDateTimeBetween(patientid, start, end);
        return appointments.stream().map(appointmentMapper::toDto).collect(Collectors.toList());

    }


    public AppointmentDto getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Not Found appointment with " + id));
        return appointmentMapper.toDto(appointment);
    }


    public List<AppointmentDto> getAllAppointment() {

        return appointmentRepository.findAll().stream().map(appointmentMapper::toDto).collect(Collectors.toList());

    }


    public void deleteAppointmentById(Long id) {

        appointmentRepository.deleteById(id);
    }


}
