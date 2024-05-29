import React, {ComponentType} from 'react'

interface PrivateRouteProps{
    component: ComponentType;
    path:string
}

function PrivateRoute({ component: Component, path, ...props }: PrivateRouteProps) {
    const isLoggedIn = true;
    if (isLoggedIn) {
        return (
            <Component />
        )
    }
    return null;
}

export default PrivateRoute