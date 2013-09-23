/** 
 * Copyright (c) Robotron Datenbanksoftware GmbH and/or its affiliates. All rights reserved.
 */
package h2;


/**
 * @author Jan Flos
 *
 */
public class Relation {
    DataBlock _dataBlock;
    String    _condition;

    public Relation(DataBlock dataBlock, String condition) {
        super();
        _dataBlock = dataBlock;
        _condition = condition;
    }

    /**
     * @return the dataBlock
     */
    public DataBlock getDataBlock() {
        return _dataBlock;
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return _condition;
    }

}
