create table payments(



    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    invoice_id BIGINT NOT NULL ,

    amount DECIMAL(10, 2) NOT NULL,

   change_amount DECIMAL(10,2),

    status VARCHAR(20) NOT NULL,

    method VARCHAR(20) NOT NULL,

    payment_date TIMESTAMP NOT NULL,


    CONSTRAINT fk_payment_invoice
        FOREIGN KEY (invoice_id)
        REFERENCES invoice(id)
        ON DELETE RESTRICT



);