from fastapi import FastAPI, Depends, HTTPException
from typing import Optional
from pydantic import BaseModel,Field
import models
import tran
import splits
import groups
import json
from database import engine, SessionLocal,enginee, SessionLocall,engineee, SessionLocalll,engineeee, SessionLocallll
from sqlalchemy.orm import Session
from auth import AuthHandler
#from schemas import AuthDetails


app = FastAPI()


auth_handler = AuthHandler()
models.Base.metadata.create_all(bind=engine)

def get_db():
    try:
        db=SessionLocal()
        yield db
    finally:
        db.close()

tran.Base.metadata.create_all(bind=enginee)

def get_dbb():
    try:
        dbb=SessionLocall()
        yield dbb
    finally:
        dbb.close()


splits.Base.metadata.create_all(bind=engineee)

def get_dbbb():
    try:
        dbbb=SessionLocalll()
        yield dbbb
    finally:
        dbbb.close()


groups.Base.metadata.create_all(bind=engineeee)

def get_dbbbb():
    try:
        dbbbb=SessionLocallll()
        yield dbbbb
    finally:
        dbbbb.close()




class Tran(BaseModel):

    
    tranno:int
    From:str
    To:str
    Amount:int
    status:str
    tranid:int

class User(BaseModel):
    
    name:str
    password:str
    

class Split(BaseModel):

    no:int
    splitname:str
    Creator:str
    People:str
    Payment:int

class Group(BaseModel):
    
    sno:int
    Groupname:str
    Creator:str
    People:str





@app.post('/register', status_code=201)
def register(auth_details: User,db: Session=Depends(get_db)):
    users=db.query(models.Users).all()
    b=0
    for x in users:    
        if x.name == auth_details.name:
            b=1
            
        else:
            pass
            
    if b==1:
        return {"output":"username already taken , kindly choose a different username"}
    
    else:
         if (auth_details.name=="") or (auth_details.password=="") :
            return {"output":"Username/password cant be empty"}   
            
            
         else:   
            hashed_password = auth_handler.get_password_hash(auth_details.password)
            user_model=models.Users()
            user_model.name=auth_details.name
            user_model.password=auth_details.password
            #user_model.transactions=auth_details.transactions- list is unhashable , could not be key 
            
            db.add(user_model)
            db.commit()

            return {"output":"Account created successfully , kindly login"}


@app.post('/login')
def login(auth_details:User,db: Session=Depends(get_db)):
    user = None
    users=db.query(models.Users).all()
    for x in users:
        if x.name == auth_details.name:
            if x.password ==auth_details.password:
                user = x
                user.password=auth_handler.get_password_hash(auth_details.password)
                break
    
    if (user is None) or (not auth_handler.verify_password(auth_details.password, user.password)):
        return { 'token': "a","msg":"Invalid username/password" }

    else :
        token = auth_handler.encode_token(user.name)
        return { 'token': token,"msg":"login successful" }
        
        



@app.get('/user/{namez}')
def userinfo(namez:str,db: Session=Depends(get_db),username=Depends(auth_handler.auth_wrapper)):
    users=db.query(models.Users).all()
    a=0
    for x in users:    
        if x.name== namez:
            a=1
            y=x
        else:
            pass
    if a==1:
        return y
    else:
        return "username invalid"


@app.get('/users')
def users(db: Session=Depends(get_db)):
    return db.query(models.Users).all()

@app.delete("/del/{name}")

#to replace/update the blog list when the required name is found

def del_name(name:str,db: Session=Depends(get_db)):
    
    u_model=db.query(models.Users).filter(models.Users.name==name).first()

    if u_model is None:
        return "no items found"
    
    db.query(models.Users).filter(models.Users.name==name).delete()
    db.commit()

@app.get('/transactions')
def transactions(dbb: Session=Depends(get_dbb)):
    return dbb.query(tran.Trans).all()

@app.post('/addtran')
def addt(t:Tran,dbb: Session=Depends(get_dbb),db: Session=Depends(get_db)):
    users=db.query(models.Users).all()
    b=0
    a=0
    for x in users:    
        if x.name == t.From:
            b=1           
        else:
            pass
        if x.name == t.To:
            a=1        
        else:
            pass
            
    if a*b==1:
        t_model=tran.Trans()
        
        t_model.tranno=t.tranno
        t_model.From=t.From
        t_model.To=t.To
        t_model.Amount=t.Amount
        t_model.status=t.status
        t_model.tranid=t.tranid
        dbb.add(t_model)
        dbb.commit()
        z=""
        if(t.status=="settle"):
            z="owes you"
        else:
            z="settle"

        tr_model=tran.Trans()
        
        tr_model.tranno=t.tranno+1
        tr_model.From=t.To
        tr_model.To=t.From
        tr_model.Amount=t.Amount
        tr_model.status=z
        tr_model.tranid=t.tranid
        dbb.add(tr_model)
        dbb.commit()
        return {"output":"transaction added successfully"}


    else:
        return {"output":"username not found , choose existing username"}

@app.delete('/deletetran/{no}')

def del_name(no:int,dbb: Session=Depends(get_dbb)):
    
    t_model=dbb.query(tran.Trans).filter(tran.Trans.tranno==no).first()

    if t_model is None:
        return "no items found"
    
    dbb.query(tran.Trans).filter(tran.Trans.tranno==no).delete()
    dbb.commit()

@app.put('/settle')
def settle(tno:int,dbb: Session=Depends(get_dbb)):
    t=dbb.query(tran.Trans).filter(tran.Trans.tranid==tno).all()
    
    
    if (t is None) :
        return {"output":"no item found"}
    else:
        for x in t:
            x.status="settled"
        
            dbb.add(x)
            dbb.commit()
        
        return {"output":"settled !! "}


@app.post('/addsplit')
def addsplit(split:Split,dbbb: Session=Depends(get_dbbb),db: Session=Depends(get_db)):
    users=db.query(models.Users).all()
    b=0
    for x in users:
        if x.name==split.People:
            b=1
        pass
             
    if b==1:
        
        s_model=splits.Splits()
        s_model.no=split.no
        s_model.splitname=split.splitname
        s_model.Creator=split.Creator
        s_model.People=split.People
        s_model.Payment=split.Payment
            
        dbbb.add(s_model)
        dbbb.commit()    
        return {"output":"split added successfully"}

    else:
        return {"output":"add usernames with which accounts exist"}

@app.get('/getsplit')
def getsplit(dbbb: Session=Depends(get_dbbb)):
    return dbbb.query(splits.Splits).all()

@app.delete('/deletesplit/{no}')
def del_split(no:int,dbbb: Session=Depends(get_dbbb)):
    
    s_model=dbbb.query(splits.Splits).filter(splits.Splits.no==no).first()

    if s_model is None:
        return "no items found"
    
    dbbb.query(splits.Splits).filter(splits.Splits.no==no).delete()
    dbbb.commit()

@app.put('/snamechange')
def settle(s:Split,dbbb: Session=Depends(get_dbbb),db: Session=Depends(get_db)):
    t=dbbb.query(splits.Splits).filter(splits.Splits.no==s.no).all()
    a=dbbb.query(splits.Splits).all()
    users=db.query(models.Users).all()
    b=0
    for x in users:
        if x.name==s.People:
            b=1
        pass
             
    if b==1:
        if (t is None) :
            return {"output":"no item found"}
        else:
            
            for x in t:
                splitn=x.splitname
                x.People=s.People
                dbbb.add(x)
                dbbb.commit()
            for y in a:
                if(y.splitname==splitn):
                    y.splitname=s.splitname
                    y.Payment=s.Payment
                    dbbb.add(y)
                    dbbb.commit()
            return {"output":"success!!"}

    else:
        return {"output":"username not found!!"}        
        
        
    
@app.post('/addgroup')
def addgroup(group:Group,dbbbb: Session=Depends(get_dbbbb),db: Session=Depends(get_db)):
    users=db.query(models.Users).all()
    b=0
    for x in users:
        if x.name==group.People:
            b=1
        pass
             
    if b==1:
        
        g_model=groups.Groups()
        g_model.sno=group.sno
        g_model.Groupname=group.Groupname
        g_model.Creator=group.Creator
        g_model.People=group.People
         
        dbbbb.add(g_model)
        dbbbb.commit()    
        return {"output":"success"}

    else:
        return {"output":"add usernames with which accounts exist"}

@app.get('/getgroup')
def getgp(dbbbb: Session=Depends(get_dbbbb)):
    return dbbbb.query(groups.Groups).all()


@app.post('/addsplitfromgroup')
def addsplitgroup(tran:Tran,split:Split,dbb: Session=Depends(get_dbb),dbbb: Session=Depends(get_dbbb),db: Session=Depends(get_db)):
    users=db.query(models.Users).all()
    b=0
    for x in users:
        if x.name==split.People:
            b=1
        pass
             
    if b==1:
        
        s_model=splits.Splits()
        s_model.no=split.no
        s_model.splitname=split.splitname
        s_model.Creator=split.Creator
        s_model.People=split.People
        s_model.Payment=split.Payment
        t_model=tran.Trans()
        
        t_model.tranno=t.tranno
        t_model.From=t.From
        t_model.To=t.To
        t_model.Amount=t.Amount
        t_model.status=t.status
        t_model.tranid=t.tranid
        dbb.add(t_model)
        dbb.commit()
        z=""
        if(t.status=="settle"):
            z="owes you"
        else:
            z="settle"

        tr_model=tran.Trans()
        
        tr_model.tranno=t.tranno+1
        tr_model.From=t.To
        tr_model.To=t.From
        tr_model.Amount=t.Amount
        tr_model.status=z
        tr_model.tranid=t.tranid
        
        dbb.add(tr_model)
        dbb.commit()
            
        dbbb.add(s_model)
        dbbb.commit()    
        return {"output":"split added successfully"}



    else:
        return {"output":"add usernames with which accounts exist"}
    


@app.delete('/deletegroup/{no}')
def del_gp(no:int,dbbbb: Session=Depends(get_dbbbb)):
    
    g_model=dbbbb.query(groups.Groups).filter(groups.Groups.sno==no).first()

    if g_model is None:
        return "no items found"
    
    dbbbb.query(groups.Groups).filter(groups.Groups.sno==no).delete()
    dbbbb.commit()

        

#@app.get('/unprotected')
#def unprotected():
 #   return { 'hello': 'world' }


#@app.get('/protected')
#def protected(username=Depends(auth_handler.auth_wrapper)):
 #   return { 'name': username }

