import  { Suspense } from 'react'
import { Routes, Route, useLocation } from 'react-router-dom'
import Login from '../pages/login/Login';
import Dashboard from '../pages/dashBoard/Dashboard';
import AdminUserManagement from '../pages/admin/AdminUserManagement';
import Admin from '../pages/admin/Admin';
import Reset from '../pages/changePassword/ChangePassword';
import Header from '../pages/header/Header';
import UserProfile from '../pages/userProfile/UserProfile';
import ForgotPassword from '../pages/forgotPassword/ForgotPassword';
import Archive from '../pages/archive/Archive';


function CustomeRoutes() {
    const location = useLocation();
    return (
        <Suspense>
           {location.pathname !== "/login" && location.pathname !== "/" && <Header />}
            <Routes>
                <Route path='/' element={<Login />} />
                <Route path='/login' element={<><Login /></>} />
                <Route path='/dashboard' element={<><Dashboard /></>} />
                <Route path='/user-manage' element={<><AdminUserManagement /></>} />
                <Route path='/admin' element={<><Admin /></>} />
                <Route path='/reset' element={<><Reset   /></>} /> 
                <Route path='/user-profile' element={<><UserProfile /></>} />                
                <Route path='/forgot-password' element={<><ForgotPassword/></>} />                
                <Route path='/archive' element={<><Archive/></>} />                
            </Routes>
        </Suspense>
    )
}

export default CustomeRoutes