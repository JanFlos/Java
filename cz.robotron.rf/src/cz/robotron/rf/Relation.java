/** 
 * Copyright (c) Robotron Datenbanksoftware GmbH and/or its affiliates. All rights reserved.
 */
package cz.robotron.rf;

import java.util.List;

/**
 * @author Jan Flos
 *
 */
public class Relation {
    DataBlock    _dataBlock;
    String       _condition;
    List<String> _boundColumnNames;

    public Relation(DataBlock dataBlock, String condition) {
        super();
        _dataBlock = dataBlock;
        _condition = condition;
        if (condition == null)
            _condition = dataBlock.getQueryDataSourceText();

        _boundColumnNames = CollectionUtils.extractTokens(_condition);
    }

    public Relation(DataBlock dataBlock) {
        this(dataBlock, null);
    }

    /**
     * @return the dataBlock
     */
    public DataBlock getDataBlock() {
        return _dataBlock;
    }

    public String getCondition() {
        return _condition;
    }

    public List<String> getBoundColumnNames() {
        return _boundColumnNames;
    }

}
