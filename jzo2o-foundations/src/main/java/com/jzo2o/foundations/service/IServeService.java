package com.jzo2o.foundations.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author author
 * @since 2025-06-19
 */
public interface IServeService extends IService<Serve> {
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    void batchAdd(List<ServeUpsertReqDTO> list);
    void updata(Long id, BigDecimal price);

    void onSale(Long id);

    void remove(Long id);

    void offSale(Long id);
    void changeHotStatus(Long id);
}
