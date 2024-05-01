DROP TABLE IF EXISTS task;

CREATE TABLE task (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      description VARCHAR(250) NOT NULL,
                      completed BOOLEAN NOT NULL DEFAULT FALSE,
                      priority ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
                      creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
                      status VARCHAR(100) NOT NULL DEFAULT 'PENDING'
);

INSERT INTO task (description, completed, priority, status) VALUES
                                                                ('Primeira tarefa', FALSE, 'MEDIUM', 'PENDING'),
                                                                ('Segunda tarefa', FALSE, 'HIGH', 'PENDING'),
                                                                ('Terceira tarefa', FALSE, 'LOW', 'PENDING');
