/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.SQLDialect.*;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;

import java.util.Set;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class BoolAnd extends DefaultAggregateFunction<Boolean> {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID = 7292087943334025737L;
    private static final Set<SQLDialect> EMULATE          = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);

    private final Condition              condition;

    BoolAnd(Condition condition) {
        super(N_BOOL_AND, BOOLEAN, DSL.field(condition));

        this.condition = condition;
    }

    @Override
    final void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {














            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (EMULATE.contains(ctx.dialect())) {
            ctx.visit(DSL.field(DSL.field("{0}", Integer.class, CustomQueryPart.of(c -> {
                c.visit(DSL.min(DSL.when(condition, one()).otherwise(zero())));
                acceptOverClause(c);
            })).eq(one())));
        }
        else
            super.accept(ctx);
    }
}
