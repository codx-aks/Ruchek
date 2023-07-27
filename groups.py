from sqlalchemy import Column,Integer,String
from database import Base

class Groups(Base):
    __tablename__ ="groups"
    sno=Column(Integer,primary_key=True)
    Groupname=Column(String)
    Creator=Column(String)
    People=Column(String)
    
    