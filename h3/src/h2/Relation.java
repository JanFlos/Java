/** 
 * Copyright (c) Robotron Datenbanksoftware GmbH and/or its affiliates. All rights reserved.
 */
package h2;

import java.util.List;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * @author Jan Flos
 *
 */
public class Relation {
    DataBlock    _dataBlock;
    String       _condition;
    List<String> _queriedColumnNames;

    public Relation(DataBlock dataBlock, String condition) {
        super();
        _dataBlock = dataBlock;
        _condition = condition;
        _queriedColumnNames = Lists.newArrayList();

        for (String token : Splitter.on('=').omitEmptyStrings().trimResults().split(_condition)) {
            if (token.startsWith(":"))
                _queriedColumnNames.add(token.substring(1).toLowerCase());

        }
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




}
