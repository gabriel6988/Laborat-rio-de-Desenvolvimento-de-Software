import React, { useState, useEffect } from 'react';
import { TodoForm } from './TodoForm';
import { TodoList } from './TodoList';
import { v4 as uuidv4 } from 'uuid';

export const TodoWrapperService = () => {
    const [todos, setTodos] = useState([]);

    // Load todos from localStorage on mount
    useEffect(() => {
        const savedTodos = JSON.parse(localStorage.getItem('todos')) || [];
        setTodos(savedTodos);
    }, []);

    // Save todos to localStorage whenever they change
    useEffect(() => {
        localStorage.setItem('todos', JSON.stringify(todos));
    }, [todos]);

    // Function to add a new todo
    const addTodo = (todoDescription) => {
        const newTodo = {
            id: uuidv4(),
            description: todoDescription,
            completed: false,
        };
        setTodos([...todos, newTodo]);
    };

    // Function to toggle completion status
    const toggleComplete = (id) => {
        setTodos(todos.map((todo) =>
            todo.id === id ? { ...todo, completed: !todo.completed } : todo
        ));
    };

    // Function to delete a todo
    const deleteTask = (id) => {
        setTodos(todos.filter((todo) => todo.id !== id));
    };

    return (
        <div className='TodoWrapper'>
            <h1>Lista de Tarefas! (Service)</h1>
            <TodoForm addTodo={addTodo} />
            {todos.map((todo) => (
                <TodoList
                    key={todo.id}
                    task={todo}
                    toggleComplete={toggleComplete}
                    deleteTask={deleteTask}
                />
            ))}
        </div>
    );
};