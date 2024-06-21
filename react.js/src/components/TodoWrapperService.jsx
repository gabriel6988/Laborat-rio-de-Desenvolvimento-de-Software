import React, { useState, useEffect } from 'react';
import { TodoForm } from './TodoForm';
import { TodoList } from './TodoList';
import { v4 as uuidv4 } from 'uuid';

export const TodoWrapperService = () => {
    const [todos, setTodos] = useState([]);

    // Carrega as tarefas do localStorage;
    useEffect(() => {
        const savedTodos = JSON.parse(localStorage.getItem('todos')) || [];
        setTodos(savedTodos);
    }, []);

    // Salva as tarefas no localStorage quando elas são alteradas;
    useEffect(() => {
        localStorage.setItem('todos', JSON.stringify(todos));
    }, [todos]);

    // Função para adicionar uma nova tarefa;
    const addTodo = (todoDescription) => {
        const newTodo = {
            id: uuidv4(),
            description: todoDescription,
            completed: false,
        };
        setTodos([...todos, newTodo]);
    };

    // Função para alternar o status de conclusão;
    const toggleComplete = (id) => {
        setTodos(todos.map((todo) =>
            todo.id === id ? { ...todo, completed: !todo.completed } : todo
        ));
    };

    // Função para deletar a tarefa;
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