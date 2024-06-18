import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import React from 'react';

export const TodoList = ({ task, toggleComplete, deleteTodo }) => {
    return (
        <div className="TodoList">
            <p
                className={`todo-text ${task.completed ? "completed" : "incompleted"}`}
                onClick={() => toggleComplete(task.id)}
            >
                {task.description}
            </p>
            <button
                className="todo-delete-button"
                onClick={() => deleteTodo(task.id)}
                aria-label={`Delete ${task.description}`}
            >
                <FontAwesomeIcon className="todo-delete-icon" icon={faTrash} />
            </button>
        </div>
    );
};