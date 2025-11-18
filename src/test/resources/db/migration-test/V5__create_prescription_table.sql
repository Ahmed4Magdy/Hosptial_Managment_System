CREATE TABLE prescription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT  NULL,
    diagnosis VARCHAR(1000) NOT NULL,
    medications VARCHAR(1000) NOT NULL,
    instructions VARCHAR(1000),
--    status ENUM('ACTIVE', 'DELETED') DEFAULT 'ACTIVE',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_prescription_doctor FOREIGN KEY (doctor_id)
        REFERENCES doctor(id) ON DELETE RESTRICT,
    CONSTRAINT fk_prescription_patient FOREIGN KEY (patient_id)
        REFERENCES patient(id) ON DELETE RESTRICT,
    CONSTRAINT fk_prescription_appointment FOREIGN KEY (appointment_id)
        REFERENCES appointment(id) ON DELETE SET NULL
);
