from sqlalchemy import Column,Integer,String
from database import Base

import mains



class Users(Base):
    __tablename__ ="delta"
    name= Column(String,primary_key=True)
    password= Column(String)
    