from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base

SQLALCHEMY_DATABASE_URL="sqlite:///./delta.db"

engine= create_engine(
    SQLALCHEMY_DATABASE_URL,connect_args={"check_same_thread":False}
)
SessionLocal = sessionmaker(autocommit=False,autoflush=False,bind=engine)
Base=declarative_base()

SQLALCHEMY_DATABASE_URLL="sqlite:///./trans.db"

enginee= create_engine(
    SQLALCHEMY_DATABASE_URLL,connect_args={"check_same_thread":False}
)
SessionLocall = sessionmaker(autocommit=False,autoflush=False,bind=enginee)
Base=declarative_base()

SQLALCHEMY_DATABASE_URLLL="sqlite:///./splitz.db"

engineee= create_engine(
    SQLALCHEMY_DATABASE_URLLL,connect_args={"check_same_thread":False}
)
SessionLocalll = sessionmaker(autocommit=False,autoflush=False,bind=engineee)
Base=declarative_base()

SQLALCHEMY_DATABASE_URLLLL="sqlite:///./groups.db"

engineeee= create_engine(
    SQLALCHEMY_DATABASE_URLLLL,connect_args={"check_same_thread":False}
)
SessionLocallll = sessionmaker(autocommit=False,autoflush=False,bind=engineeee)
Base=declarative_base()


