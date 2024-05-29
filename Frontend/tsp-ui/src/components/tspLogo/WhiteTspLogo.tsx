import React from 'react';
import BButton from 'react-bootstrap/Button';
import './bluetspLogo.css';
//image imports 
import logoT from '../../images/White_TSP_Logo_T.png';
import logoA from '../../images/White_TSP_Logo_A.png';
import logoL from '../../images/White_TSP_Logo_L.png';
import logoE from '../../images/White_TSP_Logo_E.png';
import logoN from '../../images/White_TSP_Logo_N.png';
import logoPortal from '../../images/White_TSP_Logo_portal.png';


function WhiteTspLogo() {
  return (
    <>
        <div className='logoImage'>
            <div className='logoTalent'>
                <img src= {logoT}  width="18" height="14" className="d-inline-block align-center" alt="Your Logo"/>
                <img src= {logoA}  width="18" height="14" className="d-inline-block align-center" alt="Your Logo"/>
                <img src= {logoL}  width="18" height="14" className="d-inline-block align-center" alt="Your Logo"/>
                <img src= {logoE}  width="18" height="14" className="d-inline-block align-center" alt="Your Logo"/>
                <img src= {logoN}  width="18" height="14" className="d-inline-block align-center" alt="Your Logo"/>
                <img src= {logoT}  width="18" height="14" className="d-inline-block align-center" alt="Your Logo"/>
            </div>
            <div className='logoPortal'>
                <img src= { logoPortal }  width="40" height="15" className="d-inline-block align-top" alt="Your Logo"/>
            </div>
        </div>
    </>
  );
}

export default WhiteTspLogo;