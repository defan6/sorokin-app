import React, { useState, useEffect, useMemo } from 'react';
import { Container, Table, Button, Alert, Spinner } from 'react-bootstrap';
import UserService, { User } from '../services/UserService';
import AuthService from '../services/AuthService';

const UserManagementPage: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const currentUser = useMemo(() => AuthService.getCurrentUser(), []);

    const fetchUsers = async () => {
        setLoading(true);
        try {
            const users = await UserService.getAllUsers();
            setUsers(users);
            setError(null);
        } catch (err: any) {
            console.error("Failed to fetch users", err);
            setError(err.response?.data?.message || 'Failed to load users.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleRoleChange = async (userId: number, newRole: string) => {
        if (window.confirm(`Are you sure you want to change this user's role to ${newRole}?`)) {
            try {
                await UserService.changeRole({ id: userId, role: newRole });
                // Обновляем список пользователей, чтобы отразить изменения
                fetchUsers();
            } catch (err) {
                console.error("Failed to change role", err);
                setError('Failed to change role. Please try again.');
            }
        }
    };

    if (loading) {
        return (
            <Container className="mt-4 text-center">
                <Spinner animation="border" />
            </Container>
        );
    }

    if (error) {
        return (
            <Container className="mt-4">
                <Alert variant="danger">{error}</Alert>
            </Container>
        );
    }

    return (
        <Container className="mt-4">
            <h2>User Management</h2>
            <Table striped bordered hover responsive className="table-danger border-danger">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Full Name</th>
                        <th>Roles</th>
                        <th>Photo</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.username}</td>
                            <td>{user.fullName}</td>
                            <td>{user.userRoles.map(r => r.role).join(', ')}</td>
                            <td>
                                {user.avatarUrl && (
                                    <img src={user.avatarUrl} alt="User Avatar" style={{ width: '50px', height: '50px', borderRadius: '50%', objectFit: 'cover' }} />
                                )}
                            </td>
                            <td>
                                {/* Не даем админу понизить самого себя */}
                                {user.username !== currentUser?.username && (
                                    <>
                                        {!user.userRoles.some(r => r.role === 'ROLE_ADMIN') ? (
                                            <Button variant="success" size="sm" onClick={() => handleRoleChange(user.id, 'ADMIN')}>
                                                Make Admin
                                            </Button>
                                        ) : (
                                            <Button variant="warning" size="sm" onClick={() => handleRoleChange(user.id, 'USER')}>
                                                Make User
                                            </Button>
                                        )}
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </Container>
    );
};

export default UserManagementPage;
