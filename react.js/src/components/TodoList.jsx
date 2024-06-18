import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import React from 'react';

export const TodoList = ({ task, toggleComplete, deleteTask }) => {
    return (
        <div className="Todo">
            <p
                className={`todo-text ${task.completed ? "completed" : "incompleted"}`}
                onClick={() => toggleComplete(task.id)}
            >
                {task.description}
            </p>
            <button
                className="todo-delete-button"
                onClick={() => deleteTask(task.id)}
                aria-label={`Delete ${task.description}`}
            >
                <FontAwesomeIcon className="todo-delete-icon" icon={faTrash} />
            </button>
        </div>
    );
};