DROP TABLE IF EXISTS task;

CREATE TABLE task (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      description VARCHAR(250) NOT NULL DEFAULT 'Descrição da tarefa',
                      completed BOOLEAN NOT NULL DEFAULT FALSE,
                      priority ENUM('ALTA', 'MEDIA', 'BAIXA') NOT NULL,
                      creationDate DATE NOT NULL DEFAULT CURRENT_DATE,
                      status VARCHAR(100) NOT NULL DEFAULT 'Prevista'
);

INSERT INTO task (description, completed, priority, status) VALUES
                                                                ('Primeira tarefa', FALSE, 'ALTA', 'Prevista'),
                                                                ('Segunda tarefa', FALSE, 'MEDIA', 'Prevista'),
                                                                ('Terceira tarefa', FALSE, 'BAIXA', 'Prevista');
