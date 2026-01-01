package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-06-19
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {
    /**
     * 分页查询区域服务
     *
     * @param servePageQueryReqDTO 查询条件
     * @return 分页结果
     */

    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        //通过baseMapper调用queryServeListByRegionId方法
        PageResult<ServeResDTO> serveResDTOPageResult = PageHelperUtils.selectPage(servePageQueryReqDTO, () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));
        return serveResDTOPageResult;
    }


    @Resource
    private ServeItemMapper serveItemMapper;
    @Resource
    private RegionMapper regionMapper;
    @Resource
    private ServeMapper serveMapper;

    /**
     * 批量添加区域服务
     *
     * @param list 新增列表
     */
    @Override
    @Transactional
    public void batchAdd(List<ServeUpsertReqDTO> list) {

        //1.校验服务项是否为启用状态，不是启用状态不能新增
        for (ServeUpsertReqDTO serveUpsertReqDTO : list) {
            ServeItem serveItem = serveItemMapper.selectById(serveUpsertReqDTO.getServeItemId());
            if (ObjectUtil.isNull(serveItem) || serveItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus())
                throw new ForbiddenOperationException("该服务未启用无法添加到区域下使用");

            //2.校验是否重复新增

            Serve serve = serveMapper.queryServeByRegionIdAndServeItemId(serveUpsertReqDTO.getRegionId(), serveUpsertReqDTO.getServeItemId());
            if (ObjectUtil.isNotNull(serve)) {
                throw new ForbiddenOperationException(serveItem.getName() + "服务已存在");
            }


            //3.新增服务
            Serve serve1 = BeanUtil.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serveUpsertReqDTO.getRegionId());
            serve1.setCityCode(region.getCityCode());
            baseMapper.insert(serve1);

        }
    }

    /**
     * 修改区域服务价格
     *
     * @param id    服务id
     * @param price 价格
     */
    @Override
    @Transactional
    public void updata(Long id, BigDecimal price) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("服务不存在");
        }
        //2.修改价格
        serve.setPrice(price);
        baseMapper.updateById(serve);
    }

    /**
     * 上架区域服务
     *
     * @param id 服务id
     */
    @Override
    @Transactional
    public void onSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("服务不存在");
        }
        int sale_status = serve.getSaleStatus();
        if (sale_status == FoundationStatusEnum.ENABLE.getStatus()) {
            throw new ForbiddenOperationException("服务已上架");
        }
        long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if (ObjectUtil.isNull(serveItem) || serveItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
            throw new ForbiddenOperationException("服务项未启用或不存在，无法上架");
        }
        //3.修改服务状态为上架
        serve.setSaleStatus(FoundationStatusEnum.ENABLE.getStatus());
        ;
        baseMapper.updateById(serve);
//        boolean update=lambdaUpdate()
//                .eq(Serve::getId, id)
//                .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus())
//                .update();
    }

    /**
     * 删除区域服务
     *
     * @param id 服务id
     */
    @Override
    @Transactional
    public void remove(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("服务不存在");}
        if (serve.getSaleStatus() != FoundationStatusEnum.INIT.getStatus()) {
                throw new ForbiddenOperationException("服务不是草稿状态，无法删除");
            }
            //1.删除服务
            int delete = baseMapper.deleteById(id);
            if (delete <= 0) {
                throw new ForbiddenOperationException("服务删除失败");
            }

        }
        /**
         * 下架区域服务
         *
         * @param id 服务id
         */
        @Override
        @Transactional
        public void offSale (Long id){
            Serve serve = baseMapper.selectById(id);
            if (ObjectUtil.isNull(serve)) {
                throw new ForbiddenOperationException("服务不存在");
            }
            int sale_status = serve.getSaleStatus();
            if (sale_status == FoundationStatusEnum.DISABLE.getStatus()) {
                throw new ForbiddenOperationException("服务已下架");
            }
            //3.修改服务状态为下架
            serve.setSaleStatus(FoundationStatusEnum.DISABLE.getStatus());
            baseMapper.updateById(serve);
        }
    /**
     * 设置区域服务热门
     *
     * @param id 服务id
     */
    @Override
    @Transactional
    public void changeHotStatus(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("服务不存在");
        }
        //1.修改服务热门状态
        if(serve.getIsHot()==1)
            throw new ForbiddenOperationException("服务已是热门状态");

//        LambdaUpdateWrapper<Serve> updateWrapper = Wrappers.<Serve>lambdaUpdate()
//                .set(Serve::getIsHot, 1)
//                .eq(Serve::getId, id);
//        update(updateWrapper);
//
        boolean update=lambdaUpdate().set(Serve::getIsHot, 1)
                .eq(Serve::getId, id)
                .update();
        if(!update)
            throw new ForbiddenOperationException("服务热门状态修改失败");

    }


}