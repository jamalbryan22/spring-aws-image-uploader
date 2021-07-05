import React,{useState, useEffect, useRef, useCallback} from 'react';  import {useDropzone} from 'react-dropzone'
import logo from './logo.svg';
import './App.css';
import axios from 'axios';


function App() {
  
  const [userProfiles, setUserProfiles] = useState([]);

  useEffect( ()=>{
     axios.get("http://localhost:8080/api/v1/user-profile")
    .then(res =>{
      console.log(res.data);
      setUserProfiles(res.data); 
    })
  },[]);


  function Dropzone({userProfileId}) {
    const onDrop = useCallback(acceptedFiles => {
      const file = acceptedFiles[0];
      console.log(file);
      const formData = new FormData();
      formData.append("file", file);

      axios.post(`http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`, formData, 
      {
        headers:{
          "Content-type": "multipart/form-data"
        }
      })
      .then(()=>{console.log("File uploaded successfully")})
      .catch(error => {console.log(`Error uploading file: ${error}`)})
    }, [])

    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})
  
    return (
      <div {...getRootProps()}>
        <input {...getInputProps()} />
        {
          isDragActive ?
            <p>Drop the image here ...</p> :
            <p>Drag 'n' drop profile image here, or click to select files</p>
        }
      </div>
    )
  }


  return (
    <div className="app">
      {userProfiles.map((userProfile,index)=>
      <div key={index}>
        <br/>
        <h1>{userProfile.username}</h1>
        <p>{userProfile.userProfileId}</p>
        <Dropzone{...userProfile}/>
        <br/>
      </div>
      )}
    </div>
  );
}

export default App;
