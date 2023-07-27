from sqlalchemy import Column,Integer,String
from database import Base

class Splits(Base):
    __tablename__ ="splitz"
    no=Column(Integer,primary_key=True)
    splitname=Column(String)
    Creator=Column(String)
    People=Column(String)
    Payment=Column(Integer)