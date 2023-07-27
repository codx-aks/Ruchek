from sqlalchemy import Column,Integer,String
from database import Base

class Trans(Base):
    __tablename__ ="trans"
   
    tranno= Column(Integer,primary_key=True)
    From= Column(String)
    To= Column(String)
    Amount= Column(Integer)
    status= Column(String)
    tranid=Column(Integer)

    #first element be account holder's username
    #second element be related person's username
    #third- amount
    #fourth- status of payment - in transaction