CREATE TABLE invoice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT,
    doctor_name VARCHAR(20) NOT NULL,

    patient_id BIGINT NOT NULL,
    patient_name VARCHAR(20) NOT NULL,

    appointment_id BIGINT,

    total_amount DECIMAL(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, PAID, CANCELED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL,

    CONSTRAINT fk_invoice_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
    CONSTRAINT fk_invoice_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    CONSTRAINT fk_invoice_appointment FOREIGN KEY (appointment_id) REFERENCES appointment(id)
);
