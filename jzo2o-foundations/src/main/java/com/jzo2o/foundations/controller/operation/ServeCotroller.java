package com.jzo2o.foundations.controller.operation;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.model.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ConfigRegionSetReqDTO;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ConfigRegionResDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IConfigRegionService;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 区域服务表
 * </p>
 *
 * @author itcast
 * @since 2025-06-16
 */
@Validated
@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务管理相关接口")
public class ServeCotroller {

    @Resource
    private IServeService serveService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询区域服务列表", notes = "分页查询区域服务列表")
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        PageResult<ServeResDTO> page = serveService.page(servePageQueryReqDTO);
        return page;
    }


    @PostMapping("/batch")
    @ApiOperation(value = "批量添加区域服务", notes = "批量添加区域服务")
    public void batchAdd(@RequestBody List<ServeUpsertReqDTO> list) {
        serveService.batchAdd(list);
    }

    @PutMapping("/{id}")
    @ApiOperation("区域服务价格修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    public void updata(@PathVariable("id") Long id, @RequestParam("price") BigDecimal price) {
        serveService.updata(id, price);
    }


    @PutMapping("/onSale/{id}")
    @ApiOperation(value = "上架区域服务", notes = "上架区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void onSale(@PathVariable("id") Long id) {

        serveService.onSale(id);
    }

    @DeleteMapping("/serve/{id}")
    @ApiOperation(value = "删除区域服务", notes = "删除区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void delete(@PathVariable("id") Long id) {
        serveService.remove(id);
    }

    @PutMapping("/offSale/{id}")
    @ApiOperation(value = "下架区域服务", notes = "下架区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void offSale(@PathVariable("id") Long id) {

        serveService.offSale(id);
    }
    @PutMapping("/setHot/{id}")
    @ApiOperation(value = "设置热门", notes = "设置热门服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void changeHotStatus(@PathVariable("id") Long id) {
        serveService.changeHotStatus(id);
    }

}
