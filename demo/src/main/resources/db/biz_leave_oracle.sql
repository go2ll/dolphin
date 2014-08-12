 
  
  CREATE TABLE DOLPHIN_EXAMPLE_LEAVE
 (
    ID            NUMBER          NOT NULL , 
    APPLIER       VARCHAR2(20)        NULL , 
    LEAVE_DAY     CHAR(1)             NULL , 
    APPLY_DATE    DATE                NULL , 
    REASON        CLOB                NULL , 
    APPROVER      NUMBER(30)          NULL , 
    APPROVE_FLAG  VARCHAR(10)         NULL ,
    PROCESS_INS_ID NUMBER(30)         NULL ,
    CONSTRAINT PK_LEAVE PRIMARY KEY (ID)
 )

