import { Navbar, Container, Nav } from 'react-bootstrap';
import './header.css';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store/rootReducer';
import { clearUser } from '../../store/userReducer/userActions';
import { useNavigate } from 'react-router-dom'
import logout from '../../images/logoutIcon3.png';
import tspLogo from '../../images/white_TSP_logo.png';

function Header() {
  const user = useSelector((state: RootState) => state.user.user);

  const dispatch = useDispatch()
  const navigate = useNavigate()

  const handleLogin = () => {
    dispatch(clearUser());
    navigate('/login')
  }

  return (
    <>
      <Navbar expand="lg" className="custom-navbar">
        <Container>
          <Navbar.Brand href="/" className='textProps left-right'>
            <img src={tspLogo} width="80" height="30" className="d-inline-block align-top" alt="Your Logo" />
          </Navbar.Brand>

          <div className='userInfo_parent'>
            {user?.emailId &&
              <>
                <Navbar.Brand className='UserInfo textProps'>User : {user?.name}</Navbar.Brand>
                <Navbar.Brand className='UserInfo textProps'>UserGroup:{user?.userGroup?.ugname}</Navbar.Brand>
              </>
            }
          </div>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="ms-auto">
              {user?.emailId &&
              <Nav.Link className='textProps left-right'>
                <img src={logout} width="30" height="30" className="d-inline-block align-top" alt="Your Logo"  onClick={handleLogin}/>
              </Nav.Link>
                }
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
};

export default Header;