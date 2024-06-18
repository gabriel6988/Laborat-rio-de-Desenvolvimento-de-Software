import React, { useState, useEffect } from 'react';
import { TodoForm } from './TodoForm';
import { TodoList } from './TodoList';
import { v4 as uuidv4 } from 'uuid';

export const TodoWrapper = () => {
    const [todos, setTodos] = useState([]);

    useEffect(() => {
        const savedTodos = JSON.parse(localStorage.getItem('todos')) || [];
        setTodos(savedTodos);
    }, []);

    useEffect(() => {
        localStorage.setItem('todos', JSON.stringify(todos));
    }, [todos]);

    const addTodo = (todoDescription) => {
        const newTodo = {
            id: uuidv4(),
            description: todoDescription,
            completed: false,
        };
        setTodos([...todos, newTodo]);
    };

    const toggleComplete = (id) => {
        const newTodos = todos.map((todo) =>
            todo.id === id ? { ...todo, completed: !todo.completed } : todo
        );
        setTodos(newTodos);
    };

    const deleteTodo = (id) => {
        const newTodos = todos.filter((todo) => todo.id !== id);
        setTodos(newTodos);
    };

    const editTodo = (id, newDescription) => {
        const newTodos = todos.map((todo) =>
            todo.id === id ? { ...todo, description: newDescription } : todo
        );
        setTodos(newTodos);
    };

    return (
        <div className='TodoWrapper'>
            <h1>Lista de Tarefas</h1>
            <TodoForm addTodo={addTodo} />
            {todos.map((todo) => (
                <TodoList
                    key={todo.id}
                    task={todo}
                    toggleComplete={toggleComplete}
                    deleteTodo={deleteTodo}
                    editTodo={editTodo}
                />
            ))}
        </div>
    );
};