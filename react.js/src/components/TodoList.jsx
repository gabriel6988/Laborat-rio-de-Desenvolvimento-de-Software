import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash, faPenToSquare, faSave } from '@fortawesome/free-solid-svg-icons';
import React, { useState } from 'react';

export const TodoList = ({ task, toggleComplete, deleteTodo, editTodo }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [newDescription, setNewDescription] = useState(task.description);

    const handleEditChange = (e) => {
        setNewDescription(e.target.value);
    };

    const handleEditSubmit = () => {
        editTodo(task.id, newDescription);
        setIsEditing(false);
    };

    return (
        <div className="TodoList">
            {isEditing ? (
                <input
                    type="text"
                    value={newDescription}
                    onChange={handleEditChange}
                    className="todo-edit-input"
                />
            ) : (
                <p
                    className={`todo-text ${task.completed ? "completed" : "incompleted"}`}
                    onClick={() => toggleComplete(task.id)}
                >
                    {task.description}
                </p>
            )}
            <div className="todo-actions">
                {isEditing ? (
                    <button
                        className="todo-save-button"
                        onClick={handleEditSubmit}
                        aria-label="Save changes"
                    >
                        <FontAwesomeIcon icon={faSave} />
                    </button>
                ) : (
                    <button
                        className="todo-edit-button"
                        onClick={() => setIsEditing(true)}
                        aria-label="Edit task"
                    >
                        <FontAwesomeIcon icon={faPenToSquare} />
                    </button>
                )}
                <button
                    className="todo-delete-button"
                    onClick={() => deleteTodo(task.id)}
                    aria-label={`Delete ${task.description}`}
                >
                    <FontAwesomeIcon className="todo-delete-icon" icon={faTrash} />
                </button>
            </div>
        </div>
    );
};